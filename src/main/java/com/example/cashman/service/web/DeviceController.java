package com.example.cashman.service.web;

import com.example.cashman.Error;
import com.example.cashman.repository.dto.DeviceDTO;
import com.example.cashman.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Controller
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/device", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional(readOnly = true)
    public Iterable<DeviceDTO> getAll() {
        return deviceService.fetchAll();
    }

    @RequestMapping(value = "/device", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<DeviceDTO> initATM(@RequestBody DeviceDTO body, UriComponentsBuilder b) {
        DeviceDTO deviceDTO = deviceService.initializeATM(body);
        UriComponents uriComponents =
                b.path("/device/{id}").buildAndExpand(deviceDTO.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(deviceDTO);
    }

    @RequestMapping(value = "/device/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<DeviceDTO> findATM(@PathVariable Long id) {
        DeviceDTO atmDto = deviceService.fetchByID(id);
        if (atmDto != null)
            return ResponseEntity.ok(atmDto);
        else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/atm", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<DeviceDTO> findATM(@RequestParam String serialNumber) {
        DeviceDTO atmDto = deviceService.fetchBySerialNumber(serialNumber);
        if (atmDto != null)
            return ResponseEntity.ok(atmDto);
        else
            return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/withdraw/{id}/{withdrawAmount:.+}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<DeviceDTO> updateATM(@PathVariable Long id, @PathVariable BigDecimal withdrawAmount) {
        DeviceDTO deviceDTO = deviceService.withdraw(id, withdrawAmount);
        if (deviceDTO.getErrors().contains(Error.DEVICE_NOT_FOUND_ERROR.toString())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceDTO);
    }

    @RequestMapping(value = "/addTo/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<DeviceDTO> updateATM(@PathVariable Long id, @RequestBody DeviceDTO body, UriComponentsBuilder b) {
        DeviceDTO deviceDTO = deviceService.addTo(id, body);
        if (deviceDTO.getErrors().contains(Error.WITHDRAWAL_ERROR.toString())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deviceDTO);
    }


}
