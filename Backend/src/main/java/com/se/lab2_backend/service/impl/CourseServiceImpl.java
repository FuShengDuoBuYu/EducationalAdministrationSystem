package com.se.lab2_backend.service.impl;


import com.alibaba.fastjson.JSON;
import com.se.lab2_backend.common.*;
import com.se.lab2_backend.entity.*;
import com.se.lab2_backend.mapper.*;
import com.se.lab2_backend.service.*;
import com.se.lab2_backend.util.ConstVariable;
import com.se.lab2_backend.util.Response;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private static CourseMajorMapper courseMajorMapper;
    MajorService majorService;
    AdminService adminService;
    TeacherService teacherService;
    ClassroomService classroomService;
    CourseMapper courseMapper;
    AvailableTypeMapper availableTypeMapper;
    TimetableMapper timetableMapper;
    ScheduleMapper scheduleMapper;
    CourseApplicationMapper courseApplicationMapper;
    ApplicationResultMapper applicationResultMapper;
    ApplicationTypeMapper applicationTypeMapper;
    StudentCourseMapper studentCourseMapper;

    @Autowired
    public void setCourseMajorMapper(CourseMajorMapper courseMajorMapper) {
        CourseServiceImpl.courseMajorMapper = courseMajorMapper;
    }

    enum CheckType{
        NEW, MODIFY
    }
    @Autowired
    public CourseServiceImpl(MajorService majorService, AdminService adminService, TeacherService teacherService, ClassroomService classroomService, CourseMapper courseMapper, AvailableTypeMapper availableTypeMapper, TimetableMapper timetableMapper, ScheduleMapper scheduleMapper, CourseApplicationMapper courseApplicationMapper, ApplicationResultMapper applicationResultMapper, ApplicationTypeMapper applicationTypeMapper, StudentCourseMapper studentCourseMapper) {
        this.majorService = majorService;
        this.adminService = adminService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
        this.courseMapper = courseMapper;
        this.availableTypeMapper = availableTypeMapper;
        this.timetableMapper = timetableMapper;
        this.scheduleMapper = scheduleMapper;
        this.courseApplicationMapper = courseApplicationMapper;
        this.applicationResultMapper = applicationResultMapper;
        this.applicationTypeMapper = applicationTypeMapper;
        this.studentCourseMapper = studentCourseMapper;
    }

    //TODO: ?????????????????????
    //???Timetable?????????????????????????????????????????????(??????????????????????????????)????????????TimetableVO
    public static List<TimetableVO> getTimetableList(Course course) {
        Map<String, List<Timetable>> placeAndDayList = course.getTimetableList().stream()
                                                .collect(Collectors.groupingBy(item -> item.getDay() + " " + item.getClassroom()
                                                ));
        //???????????????????????????
        ArrayList<TimetableVO> timetableList = new ArrayList<>();
        placeAndDayList.forEach((name, list) -> {
            Map<Integer, List<Integer>> weekList = list.stream().collect(Collectors.groupingBy(Timetable::getWeek, Collectors.mapping(Timetable::getSession, Collectors.toList())));
            String[] dayAndPlaceInfo = name.split("\\s+");// ??????: "?????? ????????? ??????id ????????? ??????id"
            weekList.forEach((week, sessions) -> {
                TimetableVO timetableVO = new TimetableVO(week, Integer.parseInt(dayAndPlaceInfo[0]), sessions, dayAndPlaceInfo[1], dayAndPlaceInfo[2], dayAndPlaceInfo[3], dayAndPlaceInfo[4]);
                timetableList.add(timetableVO);
            });
        });
        return timetableList;
    }

    public static List<List<String>> getAvailableMajorList(Course c) {
        List<List<String>> majorList = new ArrayList<>();
        switch(c.getAvailableType().getAvailableTypeId()){
            case ConstVariable.PUBLIC_AVAILABLE_MAJOR:
                break;
            case ConstVariable.MAJOR_AVAILABLE_MAJOR:
                majorList.add(Arrays.asList(c.getMajor().getInstitute().getInstituteId(), c.getMajor().getMajorId()));
                break;
            case ConstVariable.PARTIAL_AVAILABLE_MAJOR:
                List<CourseMajor> courseMajorList = courseMajorMapper.findAllByCourseId(c.getCourseId());
                for(CourseMajor cm: courseMajorList)
                    majorList.add(Arrays.asList(cm.getMajor().getInstitute().getInstituteId(), cm.getMajor().getMajorId()));

        }
        return majorList;
    }

    public List<List<String>> getApplicationAvailableMajorList(CourseApplication c) {
        List<List<String>> majorList = new ArrayList<>();
        switch(c.getAvailableType().getAvailableTypeId()){
            case ConstVariable.PUBLIC_AVAILABLE_MAJOR:
                break;
            case ConstVariable.MAJOR_AVAILABLE_MAJOR:
                majorList.add(Arrays.asList(c.getMajor().getInstitute().getInstituteId(), c.getMajor().getMajorId()));
                break;
            case ConstVariable.PARTIAL_AVAILABLE_MAJOR:
                List<String> majorIdList = JSON.parseArray(c.getAvailableMajorIdList(), String.class);
                for(String s: majorIdList){
                    Major m = majorService.getMajorByMajorId(s);
                    majorList.add(Arrays.asList(m.getInstitute().getInstituteId(), m.getMajorId()));
                }
        }
        return majorList;
    }

    @Override
    public List<CourseVO> createCourseList(List<Course> courseList) {
        ArrayList<CourseVO> res = new ArrayList<>();
        for(Course c: courseList){
            res.add(new CourseVO(
                    c.getCourseId(),
                    c.getNumber(),
                    c.getIdentifier(),
                    c.getName(),
                    c.getClassHour(),
                    c.getCredit(),
                    c.getCapacity(),
                    c.getDescription(),
                    c.getTeacher().getUsername(),
                    c.getTeacher().getUuid(),
                    c.getMajor().getName(),
                    c.getMajor().getMajorId(),
                    c.getAvailableType().getName(),
                    c.getAvailableType().getAvailableTypeId(),
                    c.getMajor().getInstitute().getName(),
                    c.getMajor().getInstitute().getInstituteId(),
                    c.getYear(),
                    c.getTerm(),
                    getTimetableList(c),
                    getAvailableMajorList(c),
                    studentCourseMapper.findAllByCourseId(c.getCourseId()).size(),
                    c.isFinished()
            ));
        }
        return res;
    }

    @Override
    public List<CourseVO> getCourseList() {
        return createCourseList(courseMapper.findAll());
    }

    @Override
    public List<CourseVO> getCourseListByMajorId(String majorId) {
        return createCourseList(courseMapper.findAllByMajor(majorService.getMajorByMajorId(majorId)));
    }

    @Override
    public CourseVO getCourseVOByCourseId(String courseId) {
        List<Course> list = new ArrayList<>();
        list.add(courseMapper.findCourseByCourseId(courseId));
        return createCourseList(list).get(0);
    }

    @Override
    public List<CourseVO> getAvailableCourseListByMajorId(String majorId) {
        List<Course> availableCourseList = new ArrayList<>();
        Major major = majorService.getMajorByMajorId(majorId);
        //????????????????????????
        availableCourseList.addAll(
                courseMapper.findAllByAvailableType(
                        availableTypeMapper.findAvailableTypeByAvailableTypeId(ConstVariable.PUBLIC_AVAILABLE_MAJOR)
                )
        );
        //????????????????????????
        availableCourseList.addAll(
                courseMapper.findAllByAvailableType(
                        availableTypeMapper.findAvailableTypeByAvailableTypeId(ConstVariable.MAJOR_AVAILABLE_MAJOR))
                        .stream()
                        .filter(course -> course.getMajor().equals(major))//??????????????????????????????
                        .collect(Collectors.toList())
        );
        //??????????????????????????????
        List<CourseMajor> courseMajorList = courseMajorMapper.findAllByMajor(major);
        for(CourseMajor courseMajor: courseMajorList){
            availableCourseList.add(getCourseByCourseId(courseMajor.getCourseId()));
        }
        List<String> term = adminService.getCurrentTerm();
        List<Course> filteredCourseList = availableCourseList.stream().filter(
                course -> {return course.getYear().equals(term.get(0)) && course.getTerm().equals(term.get(1)) && !course.isFinished();}
        ).collect(Collectors.toList());
        return createCourseList(filteredCourseList);
    }
    @Override
    public List<CourseVO> getCourseListByTeacherId(String teacherId){
        return createCourseList(courseMapper.findAllByTeacher(teacherService.getTeacherByUuid(teacherId)));
    }

    @Override
    public List<CourseApplicationVO> createCourseApplicationList(List<CourseApplication> courseApplicationList) {
        ArrayList<CourseApplicationVO> res = new ArrayList<>();
        for(CourseApplication c: courseApplicationList){
            res.add(new CourseApplicationVO(
                    c.getApplicationId(),
                    c.getCourseId(),
                    c.getNumber(),
                    c.getName(),
                    c.getClassHour(),
                    c.getCredit(),
                    c.getCapacity(),
                    c.getDescription(),
                    c.getTeacher().getUsername(),
                    c.getTeacher().getUuid(),
                    c.getMajor().getName(),
                    c.getMajor().getMajorId(),
                    c.getMajor().getInstitute().getName(),
                    c.getMajor().getInstitute().getInstituteId(),
                    c.getApplicationType().getName(),
                    c.getApplicationResult().getName(),
                    parseTimetableVO(JSON.parseArray(c.getTimetableList(), TimetableRequest.class)),
                    c.getAvailableType().getName(),
                    c.getAvailableType().getAvailableTypeId(),
                    c.getYear(),
                    c.getTerm(),
                    getApplicationAvailableMajorList(c)
                    ));
        }
        return res;
    }

    private List<TimetableVO> parseTimetableVO(List<TimetableRequest> timetableRequestList) {
        List<TimetableVO> res = new ArrayList<>();
        for(TimetableRequest timetableRequest: timetableRequestList){
            Classroom classroom = classroomService.getClassroomByClassroomId(timetableRequest.getClassroomId());
            for(Integer week: timetableRequest.getWeek())
                res.add(new TimetableVO(
                        week,
                        timetableRequest.getDay(),
                        timetableRequest.getSessions(),
                        classroom.getBuilding().getName(),
                        classroom.getBuilding().getBuildingId(),
                        classroom.getName(),
                        classroom.getClassroomId())
                );
        }
        return res;
    }

    @Override
    public List<CourseApplicationVO> getCourseApplicationList() {
        return createCourseApplicationList(courseApplicationMapper.findAll());
    }

    @Override
    public List<CourseApplicationVO> getCourseApplicationList(String teacherId) {
        return createCourseApplicationList(courseApplicationMapper.findAllByTeacher(teacherService.getTeacherByUuid(teacherId)));
    }

    @Override
    public String getIdentifier(String number) {
        List<Course> list = courseMapper.findAllByNumber(number);
        int identifier = list==null?0:list.size();
        return Integer.toString(identifier+1);
    }

    Pair<List<Timetable>, Boolean> generateTimetable(Course course, List<TimetableRequest> timetableList, CheckType c){
        List<Timetable> timetables = new ArrayList<>();
        for(TimetableRequest j: timetableList){
            for(Integer week: j.getWeek())
                for(Integer session: j.getSessions()){
                    TimetableKey timetableKey = new TimetableKey(week, j.getDay(), session, j.getClassroomId(), course.getYear(), course.getTerm());
                    Timetable timetable = timetableMapper.findByTimetableId(timetableKey);
                    switch (c){
                        case NEW: //???????????????????????????????????????????????????????????????
                            if(timetable!=null) return Pair.of(new ArrayList<>(), false);
                            break;
                        case MODIFY: //?????????????????????????????????????????????????????????????????????????????????
                            if(timetable!=null && !timetable.getCourse().getCourseId().equals(course.getCourseId())) return Pair.of(new ArrayList<>(), false);
                            break;
                    }
                    timetables.add(new Timetable(timetableKey, classroomService.getClassroomByClassroomId(j.getClassroomId()), course));
                }
        }
        return Pair.of(timetables, true);
    }

    public boolean checkTimetable(String courseId, List<TimetableRequest> timetableList, CheckType c, String year, String term){
        for(TimetableRequest j: timetableList){
            for(Integer week: j.getWeek())
                for(Integer session: j.getSessions()){
                    TimetableKey timetableKey = new TimetableKey(week, j.getDay(), session, j.getClassroomId(), year, term);
                    Timetable timetable = timetableMapper.findByTimetableId(timetableKey);
                    switch (c){
                        case NEW: //???????????????????????????????????????????????????????????????
                            if(timetable!=null) return false;
                            break;
                        case MODIFY: //?????????????????????????????????????????????????????????????????????????????????
                            if(timetable!=null && !timetable.getCourse().getCourseId().equals(courseId)) return false;
                            break;
                    }
                }
        }
        return true;
    }
    @Override
    public boolean timeIsConflict(String courseId, List<TimetableRequest> timetableList){
        List<String> term = adminService.getCurrentTerm();
        return !checkTimetable(courseId,timetableList,CheckType.NEW,term.get(0),term.get(1));
    }

    @Override
    @Transactional
    public Response<?> setCoursePermission(Course course, List<String> availableMajorIdList) {
//        AvailableType availableType =  availableTypeMapper.findAvailableTypeByAvailableTypeId(availableTypeId);
//        course.setAvailableType(availableType);
        switch (course.getAvailableType().getAvailableTypeId()){
            case ConstVariable.PUBLIC_AVAILABLE_MAJOR:
            case ConstVariable.MAJOR_AVAILABLE_MAJOR:
                courseMajorMapper.deleteAllByCourseId(course.getCourseId());
                break;
            case ConstVariable.PARTIAL_AVAILABLE_MAJOR:
                if(courseMajorMapper.findAllByCourseId(course.getCourseId()) != null){
                    courseMajorMapper.deleteAllByCourseId(course.getCourseId());
                }
                List<CourseMajor> courseMajorList = new ArrayList<>();
                for(String majorId: availableMajorIdList){
                    courseMajorList.add(new CourseMajor(course.getCourseId(), majorService.getMajorByMajorId(majorId)));
                }
                courseMajorMapper.saveAll(courseMajorList);
        }
        return new Response<>(true, "?????????????????????????????????");
    }

    @Override
    public List<String> getAvailableCourseIdList() {
        List<String> term = adminService.getCurrentTerm();
        List<Course> courseList = courseMapper.findAllByYearAndTermAndFinished(term.get(0), term.get(1), false);
        return courseList.stream().map(Course::getCourseId).collect(Collectors.toList());
    }

    @Override
    public Response<?> saveCourse(Course course, List<TimetableRequest> timetableList, List<String> availableMajorIdList) {
        Response<?> res = null;
        Pair<List<Timetable>, Boolean> timetablePair = generateTimetable(course, timetableList, CheckType.NEW);
        if(!timetablePair.getSecond()) res = new Response<>(false, "????????????????????????");
        Classroom classroom = classroomService.getClassroomByClassroomId(timetableList.get(0).getClassroomId());
        if(classroom.getCapacity()<course.getCapacity()) res = new Response<>(false, "???????????????????????????");
        if(!checkSimilarity(course.getNumber(), course.getName())) res = new Response<>(false, "?????????????????????????????????????????????");
        if(res != null) return res;

        courseMapper.save(course);
        timetableMapper.saveAll(timetablePair.getFirst());
        setCoursePermission(course, availableMajorIdList);
        return new Response<>(true, "?????????????????????");
    }

    @Override
    public Response<?> updateCourse(Course course, List<TimetableRequest> timetableList, List<String> availableMajorIdList) {
        Response<?> res = null;
        Pair<List<Timetable>, Boolean> timetablePair = generateTimetable(course, timetableList, CheckType.MODIFY);
        if(!timetablePair.getSecond()) return new Response<>(false, "????????????????????????");
        Classroom classroom = classroomService.getClassroomByClassroomId(timetableList.get(0).getClassroomId());
        if(course.getCapacity()<studentCourseMapper.findAllByCourseId(course.getCourseId()).size()) res = new Response<>(false, "???????????????????????????????????????");
        if(classroom.getCapacity()<course.getCapacity()) res = new Response<>(false, "???????????????????????????");
        if(!checkSimilarity(course.getNumber(), course.getName())) res = new Response<>(false, "?????????????????????????????????????????????");
        if(res != null) return res;

        updateCourseImpl(course, timetablePair.getFirst(), availableMajorIdList);


        return new Response<>(true, "?????????????????????");
    }

    @Override
    public void updateCourseImpl(Course course, List<Timetable> timetableList, List<String> availableMajorIdList) {
        timetableMapper.deleteAllByCourse(getCourseByCourseId(course.getCourseId()));
        timetableMapper.saveAll(timetableList);
        course.setTimetableList(timetableList); // ???????????????
        courseMapper.save(course);
        setCoursePermission(course, availableMajorIdList);
    }

    private void updateSimilarCourse(Course course) {
        //NOT TODO: ?????????(?????????????????????)???????????????????????????
    }

    @Override
    public Response<?> saveCourseApplication(CourseApplication courseApplication) {
        Response<?> res = new Response<>(false, "???????????????");
        String applicationTypeId = courseApplication.getApplicationType().getApplicationTypeId();
        //??????????????????
        switch (applicationTypeId){
            case ConstVariable.NEW_APPLICATION_TYPE:
                res = saveNewApplication(courseApplication);
                break;
            case ConstVariable.MODIFY_APPLICATION_TYPE:
                res = saveModifyApplication(courseApplication);
                break;
            case ConstVariable.DELETE_APPLICATION_TYPE:
                res = saveDeleteApplication(courseApplication);
                break;
        }
        return res;
    }

    private Response<?> saveDeleteApplication(CourseApplication courseApplication) {
        if(courseApplicationMapper.findByCourseId(courseApplication.getCourseId())!=null) return new Response<>(false, "???????????????????????????????????????");
        courseApplicationMapper.save(courseApplication);
        return new Response<>(true, "???????????????????????????");
    }

    private Response<?> saveModifyApplication(CourseApplication courseApplication) {
        if(courseApplicationMapper.findByCourseId(courseApplication.getCourseId())!=null) return new Response<>(false, "???????????????????????????????????????");
        List<TimetableRequest> timetableRequests = JSON.parseArray(courseApplication.getTimetableList(), TimetableRequest.class);
        if(!checkTimetable(courseApplication.getCourseId(), timetableRequests, CheckType.MODIFY, courseApplication.getYear(), courseApplication.getTerm())) return new Response<>(false, "????????????????????????");
        Classroom classroom = classroomService.getClassroomByClassroomId(timetableRequests.get(0).getClassroomId());
        if(classroom.getCapacity()<courseApplication.getCapacity()) return new Response<>(false, "???????????????????????????");
        //???????????????????????????????????????????????????????????????
        if(!checkSimilarity(courseApplication.getNumber(), courseApplication.getName())) return new Response<>(false, "?????????????????????????????????????????????");
        courseApplicationMapper.save(courseApplication);
        return new Response<>(true, "???????????????????????????", courseApplication);
    }

    private Response<?> saveNewApplication(CourseApplication courseApplication) {
        List<TimetableRequest> timetableRequests = JSON.parseArray(courseApplication.getTimetableList(), TimetableRequest.class);
        if(!checkTimetable("", timetableRequests, CheckType.NEW, courseApplication.getYear(), courseApplication.getTerm())) return new Response<>(false, "????????????????????????");
        Classroom classroom = classroomService.getClassroomByClassroomId(timetableRequests.get(0).getClassroomId());
        if(classroom.getCapacity()<courseApplication.getCapacity()) return new Response<>(false, "???????????????????????????");
        //???????????????????????????????????????????????????????????????
        if(!checkSimilarity(courseApplication.getNumber(), courseApplication.getName())) return new Response<>(false, "?????????????????????????????????????????????");
        courseApplicationMapper.save(courseApplication);
        return new Response<>(true, "???????????????????????????", "111");
    }

    //TODO: ??????????????????????????????????????????????????? ??????????????????false
    private boolean checkSimilarity(String number, String name) {
        List<Course> similarCourseListByName = courseMapper.findAllByName(name);
        List<Course> similarCourseListByNumber = courseMapper.findAllByNumber(number);
        boolean res = false;
        if(similarCourseListByName.size()==0 && similarCourseListByNumber.size()==0) {
            res = true;
        }
        else if(!(similarCourseListByName.size()==0) && !(similarCourseListByNumber.size()==0)){
            res = similarCourseListByName.size()==similarCourseListByNumber.size() && similarCourseListByName.get(0).getNumber().equals(number) && similarCourseListByNumber.get(0).getName().equals(name);
        }
        return res;
    }

    @Override
    @Transactional
    public Response<?> handleApplication(String applicationId, String applicationResultId) {
        CourseApplication courseApplication = courseApplicationMapper.findByApplicationId(applicationId);
        if(!courseApplication.getApplicationResult().getApplicationResultId().equals(ConstVariable.INITIAL_APPLICATION_RESULT)) return new Response<>(false, "????????????????????????");
        Response<?> res = new Response<>(false, "???????????????");
        switch (applicationResultId){
            case ConstVariable.REFUSE_APPLICATION_RESULT:
                res = refuseApplication(courseApplication);
                break;
            case ConstVariable.ACCEPT_APPLICATION_RESULT:
                res = acceptApplication(courseApplication);
                break;
        }
        if(res.isSuccess()) courseApplication.setCourseId("");
        return res;
//        return new Response<>(true, "test");
    }

    private Response<?> acceptApplication(CourseApplication courseApplication) {
        Response<?> res = new Response<>(false, "???????????????");
        courseApplication.setApplicationResult(applicationResultMapper.findByApplicationResultId(ConstVariable.ACCEPT_APPLICATION_RESULT));
        switch (courseApplication.getApplicationType().getApplicationTypeId()){
            case ConstVariable.NEW_APPLICATION_TYPE:
                res = acceptNewApplication(courseApplication);
                break;
            case ConstVariable.MODIFY_APPLICATION_TYPE:
                res = acceptModifyApplication(courseApplication);
                break;
            case ConstVariable.DELETE_APPLICATION_TYPE:
                res = acceptDeleteApplication(courseApplication);
                break;
        }
        return res;
    }

    private Response<?> acceptDeleteApplication(CourseApplication courseApplication) {
        return deleteCourseByCourseId(courseApplication.getCourseId());
    }

    private Response<?> acceptModifyApplication(CourseApplication courseApplication) {
        Course course = getCourseByCourseId(courseApplication.getCourseId());
        List<TimetableRequest> timetableList = JSON.parseArray(courseApplication.getTimetableList(), TimetableRequest.class);
        course.setNumber(courseApplication.getNumber());
        course.setName(courseApplication.getName());
        course.setClassHour(courseApplication.getClassHour());
        course.setCredit(courseApplication.getCredit());
        course.setCapacity(courseApplication.getCapacity());
        course.setDescription(courseApplication.getDescription());
        course.setMajor(majorService.getMajorByMajorId(courseApplication.getMajor().getMajorId()));
        course.setTeacher(teacherService.getTeacherByUuid(courseApplication.getTeacher().getUuid()));
        course.setAvailableType(availableTypeMapper.findAvailableTypeByAvailableTypeId(courseApplication.getAvailableType().getAvailableTypeId()));
        course.setYear(courseApplication.getYear());
        course.setTerm(courseApplication.getTerm());
        return updateCourse(course, timetableList, JSON.parseArray(courseApplication.getAvailableMajorIdList(), String.class));
    }

    private Response<?> acceptNewApplication(CourseApplication courseApplication) {
        Course course = new Course(
                courseApplication.getNumber(),
                courseApplication.getName(),
                courseApplication.getClassHour(),
                courseApplication.getCredit(),
                courseApplication.getCapacity(),
                courseApplication.getDescription(),
                majorService.getMajorByMajorId(courseApplication.getMajor().getMajorId()),
                teacherService.getTeacherByUuid(courseApplication.getTeacher().getUuid()),
                availableTypeMapper.findAvailableTypeByAvailableTypeId(courseApplication.getAvailableType().getAvailableTypeId()),
                courseApplication.getYear(),
                courseApplication.getTerm()
        );
        course.setFinished(false);
        List<TimetableRequest> timetableList = JSON.parseArray(courseApplication.getTimetableList(), TimetableRequest.class);
        course.setIdentifier(getIdentifier(course.getNumber()));
        return saveCourse(course, timetableList, JSON.parseArray(courseApplication.getAvailableMajorIdList(), String.class));
    }

    private Response<?> refuseApplication(CourseApplication courseApplication) {
        courseApplication.setApplicationResult(applicationResultMapper.findByApplicationResultId(ConstVariable.REFUSE_APPLICATION_RESULT));
        courseApplication.setCourseId("");
        courseApplicationMapper.save(courseApplication);
        return new Response<>(true, "?????????????????????");
    }


    @Override
    public Response<?> saveCourseTest(Course course) {
        courseMapper.save(course);
        return new Response<>(true, "?????????????????????", course);
    }

    @Override
    public Response<?> saveSchedule(List<Schedule> scheduleList) {
        scheduleMapper.deleteAll();
        scheduleMapper.saveAll(scheduleList);
        return new Response<>(true, "????????????????????????");
    }

    @Override
    public boolean getCourseSelection() {
        return (boolean) adminService.getCourseSelection().get("isCourseSelection");
    }

    @Override
    public List<Schedule> getSchedule() {
        List<Schedule> scheduleList = scheduleMapper.findAll();
        //????????? ???session????????????
        scheduleList.sort(Comparator.comparingInt(Schedule::getSession));
        return scheduleList;
    }

    @Override
    public List<ApplicationType> getApplicationType() {
        return applicationTypeMapper.findAll();
    }

    @Override
    public List<ApplicationResult> getApplicationResult() {
        return applicationResultMapper.findAll();
    }

    @Override
    public ApplicationType getApplicationTypeById(String applicationTypeId) {
        return applicationTypeMapper.findByApplicationTypeId(applicationTypeId);
    }

    @Override
    public ApplicationResult getApplicationResultById(String applicationResultId) {
        return applicationResultMapper.findByApplicationResultId(applicationResultId);
    }

    @Override
    public Course getCourseByCourseId(String courseId){
        return courseMapper.findCourseByCourseId((courseId));
    }

    @Override
    @Transactional
    public Response<?> deleteCourseByCourseId(String courseId){
        Integer delete1 = studentCourseMapper.deleteAllByCourseId(courseId);
        Integer delete = courseMapper.deleteCourseByCourseId(courseId);
        return new Response<>(delete==1,delete==1?"??????????????????":"??????????????????");
    }
    @Override
    // TODO: ??????csv???????????????lab4??????
    public Response<?> saveAll(List<Course> courses,List<List<TimetableRequest>> timetableLists){
        List<String> term = adminService.getCurrentTerm();
        for(Course c: courses){
            c.setTerm(term.get(0));
            c.setTerm(term.get(1));
            c.setAvailableType(availableTypeMapper.findAvailableTypeByAvailableTypeId(ConstVariable.PUBLIC_AVAILABLE_MAJOR));
        }
        courseMapper.saveAll(courses);
        System.out.println(timetableLists.size());
        int i=0;
        for(List<TimetableRequest>timetableList:timetableLists){
            Pair<List<Timetable>, Boolean> timetablePair = generateTimetable(courses.get(i), timetableList, CheckType.NEW);
            timetableMapper.saveAll(timetablePair.getFirst());
            System.out.println(i);
            i++;
        }
        return new Response<>(true,"??????????????????");
    }
}
