package students.marks.front.controller;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import students.marks.dal.exception.DatabaseException;
import students.marks.dal.model.Student;
import students.marks.dal.service.LabWorkService;
import students.marks.front.exception.ForbiddenException;
import students.marks.dal.model.LabWork;

import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/marks_table/api")
@AllArgsConstructor
public class LabWorksApiController {


    private final LabWorkService labWorkService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabWork.class))),

            @ApiResponse(responseCode = "400", description = "Invalid number of laboratory works")})
    @RequestMapping(value = "/lab_work",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    public ResponseEntity<LabWork> addLabWork(@RequestBody LabWork body, Principal principal) {
        if (principal == null) {
            throw new ForbiddenException();
        }

        try {
            return new ResponseEntity<LabWork>(labWorkService.add(body), HttpStatus.OK);
        } catch (DatabaseException e) {
            return new ResponseEntity<LabWork>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabWork.class))),

            @ApiResponse(responseCode = "400", description = "Invalid number of laboratory work"),

            @ApiResponse(responseCode = "404", description = "Laboratory work wasn't found")})
    @RequestMapping(value = "/lab_work",
            method = RequestMethod.DELETE)
    public ResponseEntity<LabWork> deleteLabWork(@NotNull @RequestParam(value = "lab_num", required = true) int labNum, Principal principal) {
        if (principal == null) {
            throw new ForbiddenException();
        }

        try {
            labWorkService.deleteByLabNum(labNum);
            return new ResponseEntity<LabWork>(HttpStatus.OK);
        } catch (DatabaseException e) {
            return new ResponseEntity<LabWork>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
