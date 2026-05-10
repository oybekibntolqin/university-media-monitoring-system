package uz.otfiv.universitymediamonitoringsystem.service;

import uz.otfiv.universitymediamonitoringsystem.dto.MediaEventExcelDTO;
import uz.otfiv.universitymediamonitoringsystem.entity.*;
import uz.otfiv.universitymediamonitoringsystem.exception.GlobalExceptionHandler;
import uz.otfiv.universitymediamonitoringsystem.projection.GetAllMediaEventsProjection;
import uz.otfiv.universitymediamonitoringsystem.service.interfaces.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Service
public class ExcelService {

    private final GlobalExceptionHandler globalExceptionHandler;
    private final EmployeeService employeeService;
    private final PostService postService;
    private final MediaEventService mediaEventService;
    private final MaterialService materialService;
    private final CoverageService coverageService;
    private final MediaProjectService mediaProjectService;
    private final BroadcastAndVoiceChatService broadcastAndVoiceChatService;
    private final ForeignMaterialService foreignMaterialService;

    public ExcelService(GlobalExceptionHandler globalExceptionHandler, EmployeeService employeeService, @Lazy PostService postService, MediaEventService mediaEventService, MaterialService materialService, CoverageService coverageService, MediaProjectService mediaProjectService, BroadcastAndVoiceChatService broadcastAndVoiceChatService, @Qualifier("foreignMaterialService") ForeignMaterialService foreignMaterialService) {
        this.globalExceptionHandler = globalExceptionHandler;
        this.employeeService = employeeService;

        this.postService = postService;
        this.mediaEventService = mediaEventService;
        this.materialService = materialService;
        this.coverageService = coverageService;
        this.mediaProjectService = mediaProjectService;
        this.broadcastAndVoiceChatService = broadcastAndVoiceChatService;
        this.foreignMaterialService = foreignMaterialService;
    }


    public byte[] exportEmployeesExcel() throws IOException {
        List<Employee> employees = employeeService.findAll();
        if (employees == null || employees.isEmpty()) return new byte[0];

        try (
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet("Employees");
            XSSFCellStyle style = createHeaderStyle(workbook);
            Row headerRow = sheet.createRow(0);

            generateHeader(headerRow);

            // Header cell styling
            for (int i = 0; i <= 18; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, 30 * 256);
            }

            int rowNum = 1;
            for (Employee employee : employees) {
                Row row = sheet.createRow(rowNum++);
                row.setHeightInPoints(100);
                row.createCell(0).setCellValue(rowNum - 1);

                // Organization name
                String orgName = (employee.getUser() != null && employee.getUser().getOrganization() != null) ?
                        employee.getUser().getOrganization().getName() : "Ma'lumot yo'q";
                row.createCell(1).setCellValue(orgName);

                // Attachment content
                byte[] content = (employee.getUser() != null && employee.getUser().getAttachmentContent() != null) ?
                        employee.getUser().getAttachmentContent().getContent() : null;
                if (content != null && content.length > 0) {
                    String mimeType = getMimeType(content);
                    int pictureType = getPictureType(mimeType);
                    if (pictureType != -1) {
                        int pictureIdx = workbook.addPicture(content, pictureType);
                        XSSFCreationHelper helper = workbook.getCreationHelper();
                        XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
                        XSSFClientAnchor anchor = helper.createClientAnchor();
                        anchor.setCol1(2);
                        anchor.setCol2(3);
                        anchor.setRow1(rowNum - 1);
                        anchor.setRow2(rowNum);
                        drawing.createPicture(anchor, pictureIdx);
                    }
                }

                // Full name and birthday
                String fullName = (employee.getUser() != null && employee.getUser().getFullName() != null) ?
                        employee.getUser().getFullName() : "Ma'lumot yo'q";
                row.createCell(3).setCellValue(fullName);

                String birthday = (employee.getUser() != null && employee.getUser().getBirthday() != null) ?
                        employee.getUser().getBirthday().toString() : "Ma'lumot yo'q";
                row.createCell(4).setCellValue(birthday);

                // Gender
                row.createCell(5).setCellValue("male".equals(employee.getGender()) ? "Erkak" : "Ayol");

                // Speciality
                String speciality = (employee.getSpeciality() != null && employee.getSpeciality().getSpecialities() != null) ?
                        toStringForMap(employee.getSpeciality().getSpecialities()) : "Ma'lumot yo'q";
                row.createCell(6).setCellValue(speciality);

                // Employee details
                Details details = employee.getDetails();
                row.createCell(7).setCellValue(details != null && details.getEntryDate() != null ? details.getEntryDate().toString() : "Ma'lumot yo'q");
                row.createCell(8).setCellValue(details != null && details.getLicense() != null ? details.getLicense() : "Ma'lumot yo'q");
                row.createCell(9).setCellValue(details != null && details.getQualificationInfo() != null ? details.getQualificationInfo() : "Ma'lumot yo'q");
                row.createCell(10).setCellValue(details != null && details.getWorkType() != null ? details.getWorkType().toString() : "Ma'lumot yo'q");

                // Phone
                String phone = (employee.getUser() != null && employee.getUser().getPhone() != null) ?
                        employee.getUser().getPhone() : "Ma'lumot yo'q";
                row.createCell(11).setCellValue(phone);

                // Other details
                row.createCell(12).setCellValue(details != null && details.getAverageSalary() != null ? details.getAverageSalary() : "Ma'lumot yo'q");
                row.createCell(13).setCellValue(details != null && details.getAdvisor() != null ? details.getAdvisor() : "Ma'lumot yo'q");
                row.createCell(14).setCellValue(details != null && details.getDepartmentOrganisation() != null ? details.getDepartmentOrganisation() : "Ma'lumot yo'q");
                row.createCell(15).setCellValue(details != null && details.getRoom() != null ? details.getRoom() : "Ma'lumot yo'q");
                row.createCell(16).setCellValue(details != null && details.getResource() != null ? details.getResource() : "Ma'lumot yo'q");
                row.createCell(17).setCellValue(details != null && details.getBusinessTrip() != null ? details.getBusinessTrip() : "Ma'lumot yo'q");
                row.createCell(18).setCellValue(details != null && details.getSkills() != null ? details.getSkills() : "Ma'lumot yo'q");
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }


    public byte[] exportEmployeePosts(Employee employee) throws IOException {
        List<Post> employeePosts = postService.getEmployeePosts(employee.getId());
        try (
                XSSFWorkbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ) {
            Sheet sheet = workbook.createSheet("Employee Posts");
            XSSFCellStyle style = createHeaderStyle(workbook);

            Row infoRow = sheet.createRow(0);
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 8);
            sheet.addMergedRegion(cellRangeAddress);
            Cell infoCell = infoRow.createCell(0);
            infoCell.setCellValue(employee.getUser().getFullName());
            infoCell.setCellStyle(style);

            Row headerRow = sheet.createRow(1);
            generateHeaderForEmployeePosts(headerRow);
            for (int i = 0; i <= 7; i++) {
                headerRow.getCell(i).setCellStyle(style);
                sheet.setColumnWidth(i, 30 * 256);
            }

            int rowNum = 2;
            for (Post post : employeePosts) {
                Row row = sheet.createRow(rowNum++);
                fillRowForPost(row, post);
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void generateHeaderForEmployeePosts(Row headerRow) {
        String[] headers = {"Ko'rsatuvda qatnashgan OTM vakili F.I.O","Lavozimi", "Faoliyat turi", "OAV nomi", "Dastur nomi",
                "Tadbir o'tkazilgan sanasi va vaqti", "Miqyosi", "Havolasi", "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }


    private static void generateHeader(Row headerRow) {
        String[] headers = {"Number", "OTM nomi", "Rasm", "F.I.O", "Tug'ilgan sana", "Jinsi",
                "Mutaxassisligi", "Ishga qabul qilingan yili", "Attestatsiyadan o'tganligi", "Malaka oshirish kurslari",
                "Shtatdagi o'rni", "Telefon", "O'rtacha oylik maoshi", "Maslahatchi", "Bo'limi",
                "Xona", "Texnik baza", "Xorijga safarlar", "Kompetensiyasi"};
        fillHeaderRow(headerRow, headers);
    }

    private String getMimeType(byte[] content) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
            return ImageIO.getImageReaders(ImageIO.createImageInputStream(bais)).next().getFormatName();
        } catch (Exception e) {
            globalExceptionHandler.sendExceptionMessage(e);
            return "MimeType ishlamadi";
        }
    }

    private int getPictureType(String mimeType) {
        mimeType = mimeType.toLowerCase();
        return switch (mimeType) {
            case "jpg", "jpeg" -> Workbook.PICTURE_TYPE_JPEG;
            case "png" -> Workbook.PICTURE_TYPE_PNG;
            default -> -1;
        };
    }

    // Export methods for different Employee data categories
    public byte[] exportEmployeeMediaEvents(Employee employee) throws IOException {
        List<GetAllMediaEventsProjection> mediaEvents = mediaEventService.findByEmployeeId(employee.getId());

        List<MediaEventExcelDTO> dtos = getMediaEventExcelDTOS(mediaEvents);

        return exportEmployeeData(employee, dtos, "Media Tadbirlar", this::generateHeaderForMediaEvents, this::fillRowForMediaEvent);
    }

    public byte[] exportEmployeeMaterials(Employee employee) throws IOException {
        List<Material> materials = materialService.findByEmployeeId(employee.getId());
        return exportEmployeeData(employee, materials, "Materiallar", this::generateHeaderForMaterials, this::fillRowForMaterial);
    }

    public byte[] exportEmployeeCoverages(Employee employee) throws IOException {
        List<Coverage> coverages = coverageService.findByEmployeeId(employee.getId());
        return exportEmployeeData(employee, coverages, "Coverages", this::generateHeaderForCoverages, this::fillRowForCoverage);
    }

    public byte[] exportEmployeeMediaProjects(Employee employee) throws IOException {
        List<MediaProject> mediaProjects = mediaProjectService.findByEmployeeId(employee.getId());
        return exportEmployeeData(employee, mediaProjects, "Media Loyihalar", this::generateHeaderForMediaProjects, this::fillRowForMediaProject);
    }

    public byte[] exportEmployeeBroadcasts(Employee employee) throws IOException {
        List<OnlineBroadcastAndVoiceChatPost> broadcasts = broadcastAndVoiceChatService.findByEmployeeId(employee.getId());
        return exportEmployeeData(employee, broadcasts, "Broadcasts", this::generateHeaderForBroadcasts, this::fillRowForBroadcast);
    }

    public byte[] exportEmployeeForeignMaterials(Employee employee) throws IOException {
        List<ForeignMaterials> foreignMaterials = foreignMaterialService.findByEmployeeId(employee.getId());

        return exportEmployeeData(employee, foreignMaterials, "Xorijiy materiallar", this::generateHeaderForForeignMaterials, this::fillRowForForeignMaterials);
    }


    // General method for exporting Employee data
    private <T> byte[] exportEmployeeData(Employee employee, List<T> data, String sheetName,
                                          Consumer<Row> headerGenerator, BiConsumer<Row, T> rowFiller) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(sheetName);
            XSSFCellStyle style = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            headerGenerator.accept(headerRow);

            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                cell.setCellStyle(style);
                sheet.setColumnWidth(i, 30 * 256);
            }

            int rowNum = 1;
            for (T item : data) {
                Row row = sheet.createRow(rowNum++);
                rowFiller.accept(row, item);
            }
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // Header generation methods for different sheets
    private void generateHeaderForMediaEvents(Row headerRow) {
        String[] headers = {"Tadbir nomi", "Mediatadbir turi", "Raxbar xodimlarning ishtiroki",
                "Ichtimoiy tarmoqlar nomi va havolasi", "Gazeta jurnal nomi va havolasi", "Radiokanal nomi va havolasi",
                "Telekanal nomi va havolasi", "Veb sayt nomi va havolasi", "Tadbir oʻtkazilgan sanasi va vaqti", "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }

    private void generateHeaderForMaterials(Row headerRow) {
        String[] headers = {"Material mavzusi", "Material shakli", "E'lon qilingan OAV/ijtimoiy tarmoq turi",
                "Yoritilgan OAV nomi va havolasi", "E’lon qilingan sanasi", "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }

    private void generateHeaderForCoverages(Row headerRow) {
        String[] headers = {"Tadbir nomi", "Tadbir turi", "Yoritilish shakli", "E'lon qilingan OAV/ijtimoiy tarmoq turi",
                "Yoritilgan OAV nomi va havolasi", "Yoritilgan sanasi", "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }

    private void generateHeaderForMediaProjects(Row headerRow) {
        String[] headers = {"Medialoyiha nomi", "Loyiha tavfsifi", "E'lon qilingan OAV/ijtimoiy tarmoq turi",
                "Davriyligi", "Havolasi", "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }

    private void generateHeaderForBroadcasts(Row headerRow) {
        String[] headers = {"Online efir/ovozli chat mavzusi", "O'tkazilgan sanasi", "OTM rahbar hodimlarining ishtiorki",
                "Ijtimoiy tarmoq nomi", "Ishtirokchilar soni", "Havolasi", "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }

    private void generateHeaderForForeignMaterials(Row headerRow) {
        String[] headers = {
                "Tadbir nomi",
                "Yoritish shakli",
                "E'lon qilingan OAV/ijtimoiy tarmoq turi",
                "Yoritilgan OAV nomi va havolasi",
                "Yoritilgan sanasi",
                "Qo'yilgan ball"};
        fillHeaderRow(headerRow, headers);
    }

    private static void fillHeaderRow(Row headerRow, String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }
    }

    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        font.setFontName("Times New Roman");

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setWrapText(true);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THICK);
        style.setBorderTop(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);

        return style;
    }

    // Row filling methods for different Employee data categories
    private void fillRowForMediaEvent(Row row, MediaEventExcelDTO mediaEvent) {


        row.createCell(0).setCellValue(mediaEvent.getEventName());
        row.createCell(1).setCellValue(mediaEvent.getMediaEventType() != null ? mediaEvent.getMediaEventType().getValue() : "Ma'lumot yo'q");
        row.createCell(2).setCellValue(mediaEvent.getStuff() != null ? mediaEvent.getStuff().toString() : "Ma'lumot yo'q");
        row.createCell(3).setCellValue(toStringForMap(mediaEvent.getMessengers()));
        row.createCell(4).setCellValue(toStringForMap(mediaEvent.getNewspapers()));
        row.createCell(5).setCellValue(toStringForMap(mediaEvent.getRadio_channels()));
        row.createCell(6).setCellValue(toStringForMap(mediaEvent.getTv_channels()));
        row.createCell(7).setCellValue(toStringForMap(mediaEvent.getWeb_sites()));
        row.createCell(8).setCellValue(mediaEvent.getDateOfEvent().format(DateTimeFormatter.ofPattern("dd.MMMM.yyyy HH:mm")));
        row.createCell(9).setCellValue(mediaEvent.getGrade() != null ? mediaEvent.getGrade().toString() : "Ma'lumot yo'q");
    }

    private void fillRowForMaterial(Row row, Material material) {

        row.createCell(0).setCellValue(material.getTopic());
        row.createCell(1).setCellValue(material.getMaterialType() != null ? material.getMaterialType().getValue() : "Ma'lumot yo'q");
        row.createCell(2).setCellValue(material.getMassMedia());
        row.createCell(3).setCellValue(material.getSocialMediaName() + ": " + material.getLink());
        row.createCell(4).setCellValue(material.getPublishDate().toString());
        row.createCell(5).setCellValue(material.getGrade() != null ? material.getGrade().getValue().toString() : "Ma'lumot yo'q");

    }

    private void fillRowForCoverage(Row row, Coverage coverage) {


        row.createCell(0).setCellValue(coverage.getEventName());
        row.createCell(1).setCellValue(coverage.getEventType());
        row.createCell(2).setCellValue(coverage.getPublishType());
        row.createCell(3).setCellValue(coverage.getMassMedia());
        row.createCell(4).setCellValue(toStringForMap(coverage.getMediaLinks()));
        row.createCell(5).setCellValue(coverage.getPublishDate().toString());
        row.createCell(6).setCellValue(coverage.getGrade() != null ? coverage.getGrade().getValue().toString() : "Ma'lumot yo'q");

    }

    private void fillRowForMediaProject(Row row, MediaProject mediaProject) {

        row.createCell(0).setCellValue(mediaProject.getName());
        row.createCell(1).setCellValue(mediaProject.getDescription());
        row.createCell(2).setCellValue(mediaProject.getMassMedia());
        row.createCell(3).setCellValue(mediaProject.getPeriod() != null ? mediaProject.getPeriod().getName() : "Ma'lumot yo'q");
        row.createCell(4).setCellValue(mediaProject.getLink());
        row.createCell(5).setCellValue(mediaProject.getGrade() != null ? mediaProject.getGrade().getValue().toString() : "Ma'lumot yo'q");
    }

    private void fillRowForBroadcast(Row row, OnlineBroadcastAndVoiceChatPost broadcast) {

        row.createCell(0).setCellValue(broadcast.getTitle());
        row.createCell(1).setCellValue(broadcast.getEventDate().toString());

        List<String> stuffs = broadcast.getStuff();
        String joinedStuffs = (stuffs != null && !stuffs.isEmpty()) ? String.join(", ", stuffs) : "Ma'lumot yo'q";

        row.createCell(2).setCellValue(joinedStuffs);

        row.createCell(3).setCellValue(broadcast.getMessenger() != null ? broadcast.getMessenger().getValue() : "Ma'lumot yo'q");
        row.createCell(4).setCellValue(broadcast.getNumberOfPeople());
        row.createCell(5).setCellValue(broadcast.getLink());
        row.createCell(6).setCellValue(broadcast.getGrade() != null ? broadcast.getGrade().getValue().toString() : "Ma'lumot yo'q");
    }


    private void fillRowForForeignMaterials(Row row, ForeignMaterials foreignMaterials) {

        row.createCell(0).setCellValue(foreignMaterials.getTitle());
        row.createCell(1).setCellValue(foreignMaterials.getIllumination());
        row.createCell(2).setCellValue(foreignMaterials.getTypeMediaSocial());
        row.createCell(3).setCellValue(toStringForMap(foreignMaterials.getMediaNameAndLink()));
        row.createCell(4).setCellValue(foreignMaterials.getPublishedDate().toString());
        row.createCell(5).setCellValue(foreignMaterials.getGrade() != null ? foreignMaterials.getGrade().getValue().toString() : "Ma'lumot yo'q");
    }

    private String toStringForMap(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((key, value) -> {
            System.out.println("key = " + key);
            System.out.println("value = " + value);
        });
        map.forEach((key, value) -> stringBuilder.append(key).append(" : ").append(value).append("\n"));
        return stringBuilder.toString();
    }

    public byte[] exportAdminPosts(Admin admin) throws IOException {
        List<Post> posts = postService.findAdminPosts(admin.getId());
        List<GetAllMediaEventsProjection> mediaEvents = mediaEventService.findAdminMediaEvents(admin.getId());
        List<Coverage> coverages = coverageService.findAdminCoverages(admin.getId());
        List<MediaProject> mediaProjects = mediaProjectService.findAdminMediaProjects(admin.getId());
        List<Material> materials = materialService.findAdminMaterials(admin.getId());
        List<OnlineBroadcastAndVoiceChatPost> broadcasts = broadcastAndVoiceChatService.findAdminBroadcasts(admin.getId());
        List<ForeignMaterials> foreignMaterials = foreignMaterialService.findByAdminForeignMaterials(admin.getId());

        List<MediaEventExcelDTO> dtos = getMediaEventExcelDTOS(mediaEvents);


        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            XSSFCellStyle style = createHeaderStyle(workbook);

            // Har bir list uchun alohida sheet yaratish
            createSheetWithData(workbook, "Posts", posts, this::generateHeaderForEmployeePosts, this::fillRowForPost, style);
            createSheetWithData(workbook, "Media Events", dtos, this::generateHeaderForMediaEvents, this::fillRowForMediaEvent, style);
            createSheetWithData(workbook, "Coverages", coverages, this::generateHeaderForCoverages, this::fillRowForCoverage, style);
            createSheetWithData(workbook, "Media Projects", mediaProjects, this::generateHeaderForMediaProjects, this::fillRowForMediaProject, style);
            createSheetWithData(workbook, "Materials", materials, this::generateHeaderForMaterials, this::fillRowForMaterial, style);
            createSheetWithData(workbook, "Broadcasts", broadcasts, this::generateHeaderForBroadcasts, this::fillRowForBroadcast, style);
            createSheetWithData(workbook, "Foreign Materials", foreignMaterials, this::generateHeaderForForeignMaterials, this::fillRowForForeignMaterials, style);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @NotNull
    private static List<MediaEventExcelDTO> getMediaEventExcelDTOS(List<GetAllMediaEventsProjection> mediaEvents) {
        List<MediaEventExcelDTO> dtos = new ArrayList<>();

        for (GetAllMediaEventsProjection mediaEvent : mediaEvents) {
            MediaEventExcelDTO dto = new MediaEventExcelDTO();
            dto.setGetDateOfEvent(mediaEvent.getDateOfEvent());
            dto.setGetEventName(mediaEvent.getEventName());
            dto.setGetGrade(mediaEvent.getGrade());
            dto.setGetMediaEventType(mediaEvent.getMediaEventType());
            dto.setGetNewspapers(mediaEvent.getNewspapers());
            dto.setGetMessengers(mediaEvent.getMessengers());
            dto.setGetStuff(mediaEvent.getStuff());
            dto.setGetRadio_channels(mediaEvent.getRadio_channels());
            dto.setGetWeb_sites(mediaEvent.getWeb_sites());
            dto.setGetTv_channels(mediaEvent.getTv_channels());
            dtos.add(dto);
        }
        return dtos;
    }

    // createSheetWithData yordamchi metodi
    private <T> void createSheetWithData(XSSFWorkbook workbook, String sheetName, List<T> data,
                                         Consumer<Row> headerGenerator, BiConsumer<Row, T> rowFiller, XSSFCellStyle style) {
        Sheet sheet = workbook.createSheet(sheetName);

        // Header qatorini yaratish
        Row headerRow = sheet.createRow(0);
        headerGenerator.accept(headerRow);

        // Style va ustun kengligini o‘rnatish
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            cell.setCellStyle(style);
            sheet.setColumnWidth(i, 30 * 256);
        }

        // Data qatolini qo‘shish
        int rowNum = 1;
        for (T item : data) {
            Row row = sheet.createRow(rowNum++);
            rowFiller.accept(row, item);
        }
    }

    // Postlar uchun fillRow yordamchi metodi
    private void fillRowForPost(Row row, Post post) {
        row.createCell(0).setCellValue(post.getShowedUser());
        row.createCell(1).setCellValue(post.getStuff());
        row.createCell(2).setCellValue(post.getPostType());
        row.createCell(3).setCellValue(post.getMedia());
        row.createCell(4).setCellValue(post.getShow());
        row.createCell(5).setCellValue(post.getDateTime().toString());
        row.createCell(6).setCellValue(post.getScale().toString());
        row.createCell(7).setCellValue(post.getLink());
        row.createCell(8).setCellValue(post.getGrade() != null ? post.getGrade().getValue().toString() : "Ma'lumot yo'q");
    }
}
