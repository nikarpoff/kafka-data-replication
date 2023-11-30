package students.marks.front.controller;

import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import students.marks.dal.exception.DatabaseException;
import students.marks.dal.model.LabWork;
import students.marks.dal.model.Student;
import students.marks.dal.service.LabWorkService;
import students.marks.dal.service.MarkService;
import students.marks.dal.service.StudentService;
import students.marks.front.exception.ForbiddenException;
import students.marks.dal.model.Mark;
import students.marks.front.model.MarkTable;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/marks_table")
public class MarksApiController {

    final MarkService markService;

    final StudentService studentService;

    final LabWorkService labWorkService;

    public MarksApiController(MarkService markService, StudentService studentService, LabWorkService labWorkService) {
        this.markService = markService;
        this.studentService = studentService;
        this.labWorkService = labWorkService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of marks returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MarkTable.class))),

            @ApiResponse(responseCode = "404", description = "Marks of all students on one subject subject not found") })
    @RequestMapping(value = "/marks",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<MarkTable>> getMarks() {
        try {
            return new ResponseEntity<List<MarkTable>>(markService.findAllStudentsWithMarksOnOneSubject(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<MarkTable>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),

            @ApiResponse(responseCode = "400", description = "Invalid student's name or number of laboratory works or mark"),

            @ApiResponse(responseCode = "404", description = "Student or number of laboratory work not found") })
    @RequestMapping(value = "/api/marks",
            consumes = { "application/json" },
            method = RequestMethod.PUT)
    public ResponseEntity<Mark> updateMark(@NotNull @RequestBody MarkTable body, Principal principal) throws DatabaseException {
        if (principal == null) {
            throw new ForbiddenException();
        }

        Mark changedMark = new Mark();

        changedMark.setStudent(studentService.findById(body.getStudentId()));
        changedMark.setLab(labWorkService.findByLabNum(body.getLabNum()));
        changedMark.setValue(body.getValue());

        System.out.println(changedMark.toString());

        try {
            return new ResponseEntity<>(markService.update(changedMark), HttpStatus.OK);
        } catch (DatabaseException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of students returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Student.class))),

            @ApiResponse(responseCode = "404", description = "Students not found") })
    @RequestMapping(value = "/students",
            produces = { "application/json" },
            method = RequestMethod.GET)
    public ResponseEntity<List<Student>> getStudents() {
        try {
            return new ResponseEntity<List<Student>>((List<Student>) studentService.listAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<List<Student>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of labs returned", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LabWork.class))),

            @ApiResponse(responseCode = "404", description = "Lab works not found")})
    @RequestMapping(value = "/labs",
            produces = {"application/json"},
            method = RequestMethod.GET)
    public ResponseEntity<List<LabWork>> getLabs() {
        try {
            return new ResponseEntity<>((List<LabWork>) labWorkService.listAll(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
