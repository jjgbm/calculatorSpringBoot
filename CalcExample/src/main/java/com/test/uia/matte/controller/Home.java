package com.test.uia.matte.controller;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author jrocha
 *
 */
@Controller
public class Home {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String greeting(@RequestParam(value = "number1", required = false) String number1,
			@RequestParam(value = "number2", required = false) String number2,
			@RequestParam(value = "mathType", required = false) String mathType, Model model) {
		if (number1 != null && number2 != null && mathType != null) {
			RestTemplate restTemplate = new RestTemplate();
			JSONObject request = new JSONObject();
			try {
				request.put("numero1", Double.parseDouble(number1));
				request.put("numero2", Double.parseDouble(number2));
			} catch (NumberFormatException e) {
				model.addAttribute("vResult", "De momento solo se admiten números para las operaciones");
				return "index";
			}

			request.put("operador", mathType);

			// set headers
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
			System.out.println(request);
			// send request and parse result
			ResponseEntity<String> result = restTemplate.exchange(
					"http://api-calculadora-calculator.192.168.99.102.nip.io/calculadora/v1/operaciones",
					HttpMethod.POST, entity, String.class);
			System.out.println(result.toString());

			if (result.getStatusCode() == HttpStatus.OK) {
				JSONObject userJson = new JSONObject(result.getBody());
				model.addAttribute("vResult", userJson.get("resultado"));
				System.out.println(userJson);
			} else {
				model.addAttribute("vResult",
						"En este momento no podemos llevar acabo su solicitud, favor intentar luego");
			}

//			switch (mathType) {
//			case "+":
//				model.addAttribute("svar", Double.parseDouble(number1) + Double.parseDouble(number2));
//				break;
//			case "*":
//				model.addAttribute("svar", Double.parseDouble(number1) * Double.parseDouble(number2));
//				break;
//			case "/":
//				if (!number2.equals("0")) {
//					model.addAttribute("svar", Double.parseDouble(number1) / Double.parseDouble(number2));
//				} else {
//					model.addAttribute("svar", "ERROR: NaN");
//				}
//				break;
//			default:
//				break;
//
//			}
		}
		return "index";
	}

}