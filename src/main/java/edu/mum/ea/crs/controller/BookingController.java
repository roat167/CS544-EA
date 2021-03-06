package edu.mum.ea.crs.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.mum.ea.crs.data.domain.Car;
import edu.mum.ea.crs.data.domain.Reservation;
import edu.mum.ea.crs.data.domain.User;
import edu.mum.ea.crs.enumeration.Role;
import edu.mum.ea.crs.service.CarService;
import edu.mum.ea.crs.service.CustomerService;
import edu.mum.ea.crs.service.ReservationService;
import edu.mum.ea.crs.service.UserContextSecurityService;
import edu.mum.ea.crs.service.UserService;

@Controller
@RequestMapping(value = "reservations")
public class BookingController extends GenericController {
	//private static final Logger logger = LoggerFactory.getLogger(BookingController.class);
	private static Logger logger = LogManager.getLogger();

	private static final String MODEL_ATTRIBUTE = "reservation";
	private static final String VIEW_DETAIL = "car/carReservation";
	private static final String VIEW_LIST = "car/reservationList";

	@Autowired
	private ReservationService reservationService;
	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerService;
	@Autowired
	private UserContextSecurityService userContextService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getAll(Model model, @ModelAttribute(MODEL_ATTRIBUTE) Reservation res) {
		if (res.getId() == null) {
			res = new Reservation();
			res.setStatus(Reservation.STATUS_PENDING);
		}		
		model.addAttribute(MODEL_ATTRIBUTE, res);
		return getView(VIEW_LIST, model);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addOrUpdate(@ModelAttribute(MODEL_ATTRIBUTE) Reservation res, Model model) {
		try {
			if (res.getStartDate().compareTo(res.getEndDate()) == 0 
					|| res.getStartDate().after(res.getEndDate())) {						
						model.addAttribute(MODEL_ATTRIBUTE, res);
						populateAttribute(model);
						setMessage("End date must be after start date");
						return getView(VIEW_DETAIL, model);
						
			}
			if (res.getId() == null) {
				model.addAttribute("msg", "Save Successfully");
				logger.info("BookingController (addOrUpdate): save new record");
			} else {
				model.addAttribute("msg", "Update Successfully");
				logger.info("BookingController (addOrUpdate): update record");
			}
			res.getCar().setStatus(Car.STATUS_NOT_AVAILABLE);
			reservationService.save(res);			
			//
		} catch (Exception e) {
			logger.error("BookingController (addOrUpdate): " + e.getMessage());
		}
		model.addAttribute(MODEL_ATTRIBUTE, res);
		populateAttribute(model);
		return getView(VIEW_DETAIL, model);

	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String detailPage(@ModelAttribute(MODEL_ATTRIBUTE) Reservation res, Model model) {
		logger.info("Booking controller detailPage() ");
		Long carId = (Long) model.asMap().get("carId");
		if (carId != null) {
			//get Current user		
			User user = userContextService.getCurrentUser();			
			if (res == null) res = new Reservation();
			res.setStatus(Reservation.STATUS_PENDING);
			res.setCar(carService.findByID(carId));
			if(user.getAccount() != null && user.getAccount().getRole() != null
					&& user.getAccount().getRole().equals(Role.USER)) res.setUser(user);
		}
		populateAttribute(model);
		return getView(VIEW_DETAIL, model);
	}

	@RequestMapping(value = "/remove", method = RequestMethod.GET)
	public String remove(Long id, Model model) {
		logger.info("Booking controller remove() ");
		reservationService.remove(id);
		return getView(VIEW_LIST, model);
	}

	@RequestMapping(value = "/u/{id}", method = RequestMethod.GET)
	public String get(@PathVariable Long id, Model model) {
		logger.info("Booking controller get() ");
		Reservation res = reservationService.findByID(id);
		model.addAttribute(MODEL_ATTRIBUTE, res);
		populateAttribute(model);
		List<Car> cars = carService.findAll();
		cars.add(res.getCar());
		model.addAttribute("cars", cars);
		return getView(VIEW_DETAIL, model);
	}

	private void populateAttribute(Model model) {
		model.addAttribute("cars", carService.findAll());
		model.addAttribute("customers", customerService.findAllCustomers());
		String[] status = { Reservation.STATUS_CANCELLED, Reservation.STATUS_COMPLETED, Reservation.STATUS_EXTENDED, Reservation.STATUS_PENDING };
		model.addAttribute("statusList", Arrays.asList(status));
	}

}
