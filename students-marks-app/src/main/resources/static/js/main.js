const app = angular.module('app', []);

app.controller('mainController', function ($scope, $http) {

    const csrfHeader = {};
    csrfHeader[document.querySelector("meta[name='csrf-header']").content]
        = document.querySelector('meta[name="csrf-token"]').content;

    console.log(csrfHeader);

    $scope.labs = [];

    let marks = [];

    $scope.mark = 0;

    $scope.students = [];
    
    this.getMarks = function() {
        this.getStudents();
        this.getLabs();
        $http.get('/marks_table/marks').then(this.successGetMarks, this.standartHandleError);
    };

    this.getStudents = function() {
        $http.get('/marks_table/students').then(this.successGetStudents, this.standartHandleError);
    };

    this.getLabs = function() {
        $http.get('/marks_table/labs').then(this.successGetLabs, this.standartHandleError);
    };

    this.addStudent = function(studentName) {
        $http.post('/marks_table/api/students', { id: 0, name: studentName }, { headers: csrfHeader } ).then(this.successAddStudent, this.standartHandleError);
    };

    this.deleteStudent = function(student) {
        $scope.deletedStudentId = student;
        $http.delete('/marks_table/api/students', {params: { student_id: student }, headers: csrfHeader }).then(this.successDeleteStudent, this.standartHandleError);
    };

    this.changeMark = function(student, labNum, mark) {
        $scope.markedStudent = student;
        $scope.markedLabNum = labNum;
        $scope.changedMarkValue = mark;

        $http.put('/marks_table/api/marks', {
            value: mark,
            student_id: student,
            lab_num: labNum
            }, { headers: csrfHeader } ).then(this.successChangeMark, this.standartHandleError);
    };

    this.addLabWork = function() {
        $http.post('/marks_table/api/lab_work', { id: 0, lab_num: $scope.labs.length + 1 }, { headers: csrfHeader } ).then(this.successAddLabWork, this.standartHandleError);
    };

    this.deleteLabWork = function() {
        $http.delete('/marks_table/api/lab_work', { params: { lab_num: $scope.labs.length }, headers: csrfHeader }).then(this.successDeleteLabWork, this.standartHandleError);
    };

    this.getMark = function(studentId, labNum) {
        return $scope.mark = marks.filter((m) => m.studentId === studentId)
                                  .filter((m) => m.labNum === labNum)[0].mark;
    }

    this.successGetMarks = function(response) {
        let marksList = response.data;

        for (let student of $scope.students) {
            for (let lab of $scope.labs) {
                let mark = marksList.find(m => m.student_id === student.value && m.lab_num === lab.value)
                let value = 0

                if (mark !== undefined) value = mark.value

                marks.push( {studentId: student.value, labNum: lab.value, mark: value} );
            }
        }
        console.log(marks)
    }

    this.successGetStudents = function(response) {
        let studentsList = response.data;

        studentsList.forEach(item => {
            const student = $scope.students.find(s => s.value === item.id);
            if (!student) {
                $scope.students.push({ value: item.id, label: item.name });
            }
        });
    }

    this.successGetLabs = function(response) {
        let labsList = response.data;

        labsList.forEach(item => {
            const lab = $scope.labs.find(l => l.value === item.lab_num);
            if (!lab) {
                $scope.labs.push({ value: item.lab_num, label: 'ЛР' + item.lab_num })
            }
        });
    }

    this.successAddStudent = function(response) {
        let addedStudent = response.data;

        $scope.students.push({ value: addedStudent.id, label: addedStudent.name });

        for (let lab of $scope.labs) {
            marks.push( {studentId: addedStudent.id, labNum: lab.value, mark: 0} )
        }
    }

    this.successDeleteStudent = function(response) {
        for (let i = 0; i < $scope.students.length; i++) {
            if ($scope.students[i].value === $scope.deletedStudentId) {
                $scope.students.splice(i, 1);
                break;
            }
        }
    }

    this.successAddLabWork = function(response) {
        let newLabNum = $scope.labs.length + 1;
        $scope.labs.push({ value: newLabNum, label: 'ЛР' + newLabNum });

        for (let student of $scope.students) {
            marks.push( {studentId: student.value, labNum: newLabNum, mark: 0} );
        }
    }

    this.successDeleteLabWork = function(response) {
        let deletedLabNum = $scope.labs.length;
        $scope.labs.pop();

        marks = marks.filter((m) => m.labNum !== deletedLabNum);
        console.log(marks);
    }

    this.successChangeMark = function(response) {
        for (let m of marks) {
            if (m.studentId ===  $scope.markedStudent && m.labNum ===  $scope.markedLabNum) {
                m.mark = $scope.changedMarkValue;
            }
        }
    }

    this.standartHandleError = function(error) {
        console.log(error);
    }
})