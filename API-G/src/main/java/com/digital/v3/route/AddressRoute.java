package com.digital.v3.route;

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
import com.digital.v3.utils.ExceptionUtils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.digital.v3.utils.HttpConnectionUtils.*;

@RestController
@Tag(name = "주소", description = "Address Related API")
@RequestMapping(value = "/rest/address")
public class AddressRoute {

	private static final String baseUrlKey = "Person";
	
	@RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "주소 등록", notes = "주소 정보를 등록하기 위한 API. *입력 필드: addressDetail")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Address.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> addressWrite (@ApiParam(value = "주소 정보", required = true) @RequestBody Address address,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestPOST(baseUrlKey, "/rest/address/write", address, token); 
			Address resAddress = (Address) jsonToObject(responseJson, Address.class);
			
			return new ResponseEntity<Address>(resAddress, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}
	
	@RequestMapping(value = "/inquiry/{addressDetail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "주소 검색", notes = "주소 상세로 주소 정보를 검색하기 위한 API.")
	@ApiResponses({
		@ApiResponse(code = 200, message = "성공", response = Address.class),
		@ApiResponse(code = 500, message = "실패", response = ErrorMsg.class)
	})
	public ResponseEntity<?> addressSearch (@ApiParam(value = "주소 상세", required = true) @PathVariable String addressDetail,
			HttpServletRequest request) {
		MultiValueMap<String, String> header = new LinkedMultiValueMap<String, String>();
		ErrorMsg errors = new ErrorMsg();
		
		try {
			String token = request.getHeader("Authorization");
			
			String responseJson = requestGET(baseUrlKey, "/rest/address/inquiry/" + encodeParams(addressDetail), token); 
			Address address = (Address) jsonToObject(responseJson, Address.class);
			
			return new ResponseEntity<Address>(address, header, HttpStatus.valueOf(200));
		} catch (ResErrorCodeException e) {
			return new ResponseEntity<String>(e.getMessage(), header, HttpStatus.valueOf(500));
		} catch (Exception e) {
			return ExceptionUtils.setException(errors, 500, e.getMessage(), header);
		}
	}

}
