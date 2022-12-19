package com.digital.v3.route;

import static com.digital.v3.utils.HttpConnectionUtils.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.digital.v3.exception.ResErrorCodeException;
import com.digital.v3.schema.Address;
import com.digital.v3.schema.ErrorMsg;
import com.digital.v3.schema.Person;
import com.digital.v3.schema.Phone;
import com.digital.v3.schema.SuccessMsg;
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "고객", description = "Person Related API")
@RequestMapping(value = "/rest/person")
public class PersonRoute {
	
	private static final String baseUrlKey = "Person";
	
	@RequestMapping(value = "/signUp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원가입", notes = "회원가입을 위한 API. *입력 필드: personName, password, gender, phoneNumber(s), addressDetail(s)")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Person.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> signUp (@ApiParam(value = "회원가입 정보", required = true) @RequestBody Person person) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String responseJson = requestPOST(baseUrlKey, "/rest/person/signUp", person); 
			Person resPerson = (Person) jsonToObject(responseJson, Person.class);
			
			return new ResponseEntity<Person>(resPerson, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "로그인", notes = "로그인을 위한 API. *입력 필드: personName, password")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> login (@ApiParam(value = "로그인 정보", required = true) @RequestBody Person person) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		try {
			String responseJson = requestPOST(baseUrlKey, "/rest/person/login", person); 
			SuccessMsg resSuccess = (SuccessMsg) jsonToObject(responseJson, SuccessMsg.class);
			
			return new ResponseEntity<SuccessMsg>(resSuccess, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "로그아웃", notes = "로그아웃을 위한 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = SuccessMsg.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> logout (HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/person/logout", token); 
			SuccessMsg resSuccess = (SuccessMsg) jsonToObject(responseJson, SuccessMsg.class);
			
			return new ResponseEntity<SuccessMsg>(resSuccess, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/byName/{personName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원 검색", notes = "회원명으로 본인 정보를 검색하기 위한 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Person.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> personSearch (@ApiParam(value = "회원명", required = true) @PathVariable String personName,
			HttpServletRequest request) throws Exception {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/person/inquiry/byName/" + encodeParams(personName), token); 
			Person resPerson = (Person) jsonToObject(responseJson, Person.class);
			
			return new ResponseEntity<Person>(resPerson, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/partyAddress/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원 주소 추가", notes = "회원의 주소 정보를 추가하기 위한 API. *입력 필드: addressId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Person.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> partyAddressAdd (@ApiParam(value = "주소 정보", required = true) @RequestBody Address address,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/person/partyAddress/add", address, token); 
			Person resPerson = (Person) jsonToObject(responseJson, Person.class);
			
			return new ResponseEntity<Person>(resPerson, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/partyAddress/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원 주소 삭제", notes = "회원의 특정 주소 정보를 삭제하기 위한 API. *입력 필드: addressId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Person.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> partyAddressDelete (@ApiParam(value = "주소 정보", required = true) @RequestBody Address address,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/person/partyAddress/delete", address, token); 
			Person resPerson = (Person) jsonToObject(responseJson, Person.class);
			
			return new ResponseEntity<Person>(resPerson, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}

	@RequestMapping(value = "/partyPhone/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원 전화번호 추가", notes = "회원의 전화번호 정보를 추가하기 위한 API. *입력 필드: phoneId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Person.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> partyPhoneAdd (@ApiParam(value = "전화번호 정보", required = true) @RequestBody Phone phone,
		HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/person/partyPhone/add", phone, token); 
			Person resPerson = (Person) jsonToObject(responseJson, Person.class);
			
			return new ResponseEntity<Person>(resPerson, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/partyPhone/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "회원 전화번호 삭제", notes = "회원의 특정 전화번호 정보를 삭제하기 위한 API. *입력 필드: phoneId")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Person.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> partyPhoneDelete (@ApiParam(value = "전화번호 정보", required = true) @RequestBody Phone phone,
		HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();

		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/person/partyPhone/delete", phone, token); 
			Person resPerson = (Person) jsonToObject(responseJson, Person.class);
			
			return new ResponseEntity<Person>(resPerson, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
}
