package uz.otfiv.universitymediamonitoringsystem.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import uz.otfiv.universitymediamonitoringsystem.entity.Employee;
import uz.otfiv.universitymediamonitoringsystem.entity.Role;
import uz.otfiv.universitymediamonitoringsystem.entity.ShowedMedia;
import uz.otfiv.universitymediamonitoringsystem.entity.User;
import uz.otfiv.universitymediamonitoringsystem.repo.MediaEventRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RunnerClass implements CommandLineRunner {
    private final RoleService roleService;
    private final OrganizationService organizationService;
    private final EmployeeService employeeService;
    private final ShowMediaService showMediaService;
    private final UserService userService;
    private final AdminService adminService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    public String ddl;
    private final MediaEventRepository mediaEventRepository;

    @Override
    public void run(String... args) throws Exception {

        if (ddl.equals("create")) {
            createEmployeeAndOrganizations();
            createTvShows();
            createRadioShows();
            createNewsPaper();
            createSocialMedia();
            createMessengerMedia();

        }
    }

    private void createTvShows() {

        ShowedMedia showedMedia = ShowedMedia.builder()
                .media(List.of(
                        "O‘zbekiston24",
                        "O‘zbekiston",
                        "Yoshlar",
                        "Toshkent",
                        "Dunyo boʻylab",
                        "Foreign Languages",
                        "Madaniyat va maʼrifat",
                        "Oʻzbekiston tarixi",
                        "Sport",
                        "Bolajon",
                        "Lux.TV",
                        "Navo",
                        "Andijon TRK",
                        "Buxoro TRK",
                        "Fargʻona TRK",
                        "Jizzax TRK",
                        "Namangan TRK",
                        "Navoiy TRK",
                        "Qashqadaryo TRK",
                        "Qoraqalpogʻiston TRK",
                        "Samarqand TRK",
                        "Sirdaryo TRK",
                        "Surxondaryo TRK",
                        "Xorazm TRK",
                        "Milliy TV",
                        "MY5",
                        "Zoʻr TV",
                        "Sevimli TV",
                        "Renessans TV",
                        "Vodiy TV",
                        "S-Iqbol TV",
                        "Istiqlol TV",
                        "Muloqot TV",
                        "Orbita TV",
                        "Qarshi TV",
                        "TTV",
                        "TV Bakharistan",
                        "Umid TV",
                        "UzTV",
                        "Zafarabod TV",
                        "Zarafshan TV",
                        "Vita TV+",
                        "Uzreport TV",
                        "Uzreport World",
                        "MTV Oʻzbekiston",
                        "Fazo TV",
                        "NTV",
                        "Ishonch TV",
                        "Jizzax TV",
                        "Nasaf TV",
                        "Margʻilon TV",
                        "Ruxsor TV",
                        "Taraqqiyot TV",
                        "TTV",
                        "Aloqa TV",
                        "AYTV",
                        "BIZ TV",
                        "FTV",
                        "8TV",
                        "Boshqa"))
                .shows(List.of(
                        "Tahlilnoma",
                        "Axborot 24",
                        "Studiya24",
                        "Maxsus reportaj",
                        "Reportaj24",
                        "Fakt24",
                        "Intervyu24",
                        "Yangi davr",
                        "7 kun",
                        "Munosabat",
                        "На прямой связи",
                        "Fan va tarix",
                        "Poytaxt",
                        "Salom, Toshkent!",
                        "Bugun",
                        "Agar...",
                        "HUDUD - MY5 TV",
                        "Zamon",
                        "Bu kun – Zo‘r tv",
                        "Millar",
                        "Ovoza",
                        "Maxsus reportaj -Uzreport",
                        "Yangiliklar - Uzreport",
                        "Iqtisod24",
                        "Gapirmasak bo‘lmaydi",
                        "Duv-duv gap",
                        "Intervyu – Milliy tv",
                        "No comment",
                        "Dunyo24",
                        "Ekologiya24",
                        "Sport24",
                        "Vaziyat24",
                        "Yangi O‘zbekiston yoshlari",
                        "Yoshlik shijoati",
                        "Tafakkur yo’li",
                        "Rostini ayting",
                        "Inson qadri",
                        "Jamuljam",
                        "Vatanparvar",
                        "Mikrofon “Yoshlar”da podcast",
                        "Assalom, O‘zbekiston!",
                        "Yangi O‘zbekiston",
                        "Yuridik klinika",
                        "Mening huquqim",
                        "O‘zbekiston uchinchi renessans ostonasida",
                        "Профи",
                        "Millat fidoyilari",
                        "Tarix tilga kirganda",
                        "Yangi taraqqiyot davri",
                        "Ochiq muloqot",
                        "Teletahlil",
                        "Zamondosh",
                        "Muallimning bir kuni",
                        "Hayot va xayol yo‘llari",
                        "Yangilanayotgan yurt",
                        "Oydin hayot LIVE",
                        "O‘zingni angla!",
                        "Biz uchun muhim",
                        "Bu ham muhim",
                        "Usturlob",
                        "Nurli rishtalar",
                        "Boshqa"))
                .postType("televediniye")
                .build();
        showMediaService.save(showedMedia);
    }

    private void createRadioShows() {
        ShowedMedia showedMedia = ShowedMedia.builder()
                .postType("radio")
                .media(List.of(
                        "O‘zbekiston24 – FM 87.9",
                        "O‘zbekiston – FM 103.1",
                        "Yoshlar – FM 104.0",
                        "Avtoradio – FM 102.0",
                        "Mahalla – FM 107.8",
                        "Andijon radiokanali – FM 101.6",
                        "Buxoro radiokanali – FM 103.9",
                        "Jizzax radiokanali – FM 105.5/106.2",
                        "Navoiy radiokanali – FM 106.6",
                        "Oltin Zamin – FM 103.6",
                        "Samarqand radiokanali – FM 105.2",
                        "Sirdaryo radiokanali – FM 98.6/90.4",
                        "Yangi Asr radiokanali – FM 102.1/101.0",
                        "Farg‘ona radiokanali – FM 90.8",
                        "Xorazm radiokanali – FM 105.0",
                        "Oltin Voha radiokanali – FM 102.3",
                        "Navro‘z radiosi – FM 88.4",
                        "A’lo FM – FM 90.0",
                        "Yoshlar ovozi – FM 90.8",
                        "Radio Oriyat – FM 100.5",
                        "Radio Grand – FM 101.5",
                        "Vodiy sadosi radiosi – FM 102.7",
                        "Poytaxt radiosi – FM 103.5",
                        "Classic Fm – FM 104.4",
                        "Radio maksima – FM 105.4",
                        "Zamin FM – FM 105.8",
                        "Oriyat dono – FM 106.5",
                        "Radio poytaxt (rus) – FM 107.2"))
                .shows(List.of(
                        "Kun mavzusi",
                        "Davr yangiliklari",
                        "Davr hafta ichida",
                        "Sport yangiliklari",
                        "Yoshlar uchun",
                        "Innovatsiya",
                        "Kamolot yo`li",
                        "Kasblar jilosi",
                        "Ma’rifat manzillari",
                        "Menga so`z bering",
                        "Mushohada maydoni",
                        "Fan olami",
                        "Oltin davr",
                        "Yuridik klinika",
                        "Ijodkor talabalar",
                        "Yoshlar bilan sayohat",
                        "Yoshlar kutubxonasi",
                        "English with Yoshlar",
                        "Yoshlikka qaytib",
                        "Yoshlar pochtasidan",
                        "Teatr va yoshlar",
                        "Hayot darsi",
                        "Boshqa"))
                .build();
        showMediaService.save(showedMedia);
    }

    private void createNewsPaper() {
        ShowedMedia showedMedia = ShowedMedia.builder()
                .postType("gazeta")
                .shows(List.of(
                        "Yangi O‘zbekiston",
                        "Правда Востока",
                        "Народное слово",
                        "XXI asr",
                        "Adolat",
                        "Ma’rifat",
                        "Yoshlar ovozi",
                        "O‘zbekiston Ovozi",
                        "Ishonch",
                        "Jamiyat",
                        "Darakchi",
                        "Toshkent oqshomi",
                        "Toshkent haftanomasi",
                        "Bekobod ovozi",
                        "Buvayda kuzgusi",
                        "Buxoro axborotnomasi",
                        "Chinoz hayoti",
                        "Chirchiq tonggi",
                        "Chirchiq",
                        "Davr",
                        "Dermatovenerologiya va reproduktiv salomatlik yangiliklari",
                        "Iqtisodiy tahlil (Ekonomicheskoye obozreniye)",
                        "Fan va turmush",
                        "Farg‘ona haqiqati",
                        "G‘uncha",
                        "Gulxan",
                        "Guliston",
                        "Hayot ko‘zgusi",
                        "Jamiyat",
                        "Jamiyat va boshqaruv",
                        "Jondor ovozi",
                        "Kesh",
                        "Mahalla",
                        "Mushtum",
                        "Nuqtai nazar",
                        "Nurli jol",
                        "Oltin vodiy",
                        "Ovozi Samarqand",
                        "Ovozi tojik",
                        "Qoraqalpog‘iston jaslari",
                        "Qoraqalpoq tonggi",
                        "Samarqand",
                        "Shifokor",
                        "Sug‘diyona",
                        "Tafakkur",
                        "Toshkent haqiqati",
                        "Zarafshon"))
                .build();
        showMediaService.save(showedMedia);
    }

    private void createSocialMedia() {
        ShowedMedia showedMedia = ShowedMedia.builder()
                .postType("internet_sites")
                .shows(List.of(
                        "Kun.uz",
                        "Daryo.uz",
                        "Gazeta.uz",
                        "Qalampir.uz",
                        "UzReport",
                        "Ziyouz.uz",
                        "Darakchi.uz",
                        "UzA",
                        "Xalq so‘zi",
                        "Yuz.uz",
                        "Uzdiplomat.uz",
                        "Hudud24.uz",
                        "Rost24.uz",
                        "Anhor.uz",
                        "Xabardor.uz",
                        "Uz24.uz",
                        "review.uz",
                        "zamin.uz",
                        "zarnews.uz",
                        "aniq.uz",
                        "nasafnews.uz",
                        "vodiy24.uz",
                        "samarkandnews.uz",
                        "karinform.uz",
                        "zamon.uz",
                        "ogoh.uz",
                        "bugun.uz",
                        "demokrat.uz",
                        "platina.uz",
                        "talimxabarlari.uz"))
                .build();
        showMediaService.save(showedMedia);
    }

    private void createMessengerMedia() {
        ShowedMedia showedMedia = ShowedMedia.builder()
                .postType("messenger")
                .shows(List.of(
                        "Telegram",
                        "Facebook",
                        "Instagram",
                        "YouTube"
                ))
                .build();
        showMediaService.save(showedMedia);
    }

    private void createEmployeeAndOrganizations() {
        //o'zgarmaydigan qism
        //Andijon
        organizationService.save(Organization.builder().name("Andijon davlat universiteti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Andijon mashinasozlik instituti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Andijon davlat tibbiyot instituti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Andijon iqtisodiyot va qurilish instituti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Andijon davlat pedagogika instituti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Andijon qishloq xo‘jaligi va agrotexnologiyalar instituti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Andijon davlat chet tillari instituti").province(Province.ANDIJON).orgType(OrgType.DAVLAT).build());
        //Buxoro
        organizationService.save(Organization.builder().name("Buxoro davlat universiteti").province(Province.BUXORO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Buxoro muhandislik-texnologiya instituti").province(Province.BUXORO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Buxoro davlat pedagogika instituti").province(Province.BUXORO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Buxoro davlat tibbiyot instituti").province(Province.BUXORO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("“TIQXMMI” MTU Buxoro tabiiy resurslarni boshqarish instituti").province(Province.BUXORO).orgType(OrgType.DAVLAT).build());
        //Namangan
        organizationService.save(Organization.builder().name("Namangan davlat universiteti").province(Province.NAMANGAN).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Namangan muhandislik-qurilish instituti").province(Province.NAMANGAN).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Namangan muhandislik-texnologiya instituti").province(Province.NAMANGAN).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Namangan davlat chet tillari instituti").province(Province.NAMANGAN).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Namangan davlat pedagogika instituti").province(Province.NAMANGAN).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Namangan to‘qimachilik sanoati instituti").province(Province.NAMANGAN).orgType(OrgType.DAVLAT).build());
        //Samarqand
        organizationService.save(Organization.builder().name("Samarqand davlat universiteti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat arxitektura-qurilish universiteti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat universitetining Kattaqoʻrgʻon filiali").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat chet tillar instituti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat tibbiyot universiteti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat veterinariya meditsinasi, chorvachilik va biotexnologiyalar universiteti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand iqtisodiyot va servis instituti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand agroinnovatsiyalar va tadqiqotlar instituti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat iqtisodiyot universiteti Samarqand filiali").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat universitetining Urgut filiali").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti Samarqand filiali").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston-Finlyandiya pedagogika instituti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("“Ipak yo‘li” turizm va madaniy meros xalqaro universiteti").province(Province.SAMARQAND).orgType(OrgType.DAVLAT).build());
        //Toshkent
        organizationService.save(Organization.builder().name("Toshkent davlat o‘zbek tili va adabiyoti universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston xalqaro islom akademiyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("A.Avloniy nomidagi pedagogik mahorat milliy instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston Milliy universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat texnika universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat jahon tillari universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat sharqshunoslik universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent arxitektura-qurilish universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent toʻqimachilik va yengil sanoat instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat transport universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent kimyo-texnologiya instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat iqtisodiyot universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Jahon iqtisodiyoti va diplomatiya universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Chirchiq davlat pedagogika universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat pedagogika universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent tibbiyot akademiyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat texnika universiteti Olmaliq filiali").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat stomatologiya instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent pediatriya tibbiyot instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent Farmatsevtika instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat agrar universiteti").province(Province.TOSHKENT_VILOYATI).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("“Toshkent irrigatsiya va qishloq xo‘jaligini mexanizatsiyalash muhandislari instituti” Milliy tadqiqot universiteti").province(Province.TOSHKENT_VILOYATI).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat san’at va madaniyat instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat konservatoriyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti").province(Province.TOSHKENT_VILOYATI).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat jismoniy tarbiya va sport universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat veterinariya meditsinasi, chorvachilik va biotexnologiyalar universiteti Toshkent filiali").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat yuridik universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston jurnalistika va ommaviy kommunikatsiyalar universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Oʻzbekiston davlat sport akademiyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti Nurafshon filiali").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Geologiya fanlari universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbek milliy musiqa sanʼati instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Milliy estrada san’ati instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Jamoat xavfsizligi universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat xoreografiya akademiyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Milliy rassomlik va dizayn instituti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Huquqni muhofaza qilish akademiyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Biznes va tadbirkorlik oliy maktabi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Yangi O‘zbekiston universiteti").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Bank-moliya akademiyasi").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Markaziy Osiyo atrof-muhit va iqlim o‘zgarishini o‘rganish universiteti (Central Asian Green University)").province(Province.TOSHKENT).orgType(OrgType.DAVLAT).build());
        //Sirdaryo
        organizationService.save(Organization.builder().name("Guliston davlat pedagogika instituti").province(Province.SIRDARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Guliston davlat universiteti").province(Province.SIRDARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent kimyo-texnologiya instituti Yangiyer filiali").province(Province.SIRDARYO).orgType(OrgType.DAVLAT).build());
        //Jizzax
        organizationService.save(Organization.builder().name("Jizzax politexnika instituti").province(Province.JIZZAX).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Jizzax davlat pedagogika universiteti").province(Province.JIZZAX).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston Milliy universiteti Jizzax filiali").province(Province.JIZZAX).orgType(OrgType.DAVLAT).build());
        //NUKUS
        organizationService.save(Organization.builder().name("Nukus konchilik instituti").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Qoraqalpog‘iston qishloq xo‘jaligi va agrotexnologiyalar instituti").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Oʻzbekiston davlat konservatoriyasi Nukus filiali").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Qoraqalpoq davlat universiteti").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Qoraqalpog‘iston tibbiyot instituti").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Nukus davlat pedagogika instituti").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti Nukus filiali").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat san’at va madaniyat instituti Nukus filiali").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Samarqand davlat veterinariya meditsinasi, chorvachilik va biotexnologiyalar universiteti Nukus filiali").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat jismoniy tarbiya va sport universiteti Nukus filiali").province(Province.QORAQALPOGISTON).orgType(OrgType.DAVLAT).build());
        //Farg'ona
        organizationService.save(Organization.builder().name("Oʻzbekiston davlat jismoniy tarbiya va sport universiteti Fargʻona filiali").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Farg‘ona davlat universiteti").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Farg‘ona politexnika instituti").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Farg‘ona jamoat salomatligi tibbiyot instituti").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Qo‘qon davlat pedagogika instituti").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent davlat texnika universiteti Qo‘qon filiali").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("O‘zbekiston davlat san’at va madaniyat institutining Farg‘ona mintaqaviy filiali").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti Farg‘ona filiali").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Oziq-ovqat texnologiyasi va muhandisligi xalqaro instituti").province(Province.FARGONA).orgType(OrgType.DAVLAT).build());
        //Navoiy
        organizationService.save(Organization.builder().name("Navoiy davlat konchilik va texnologiyalar universiteti").province(Province.NAVOIY).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Navoiy davlat pedagogika instituti").province(Province.NAVOIY).orgType(OrgType.DAVLAT).build());
        //Xorazm
        organizationService.save(Organization.builder().name("Oʻzbekiston davlat xoreografiya akademiyasi Urganch filiali").province(Province.XORAZM).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent tibbiyot akademiyasi Urganch filiali").province(Province.XORAZM).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Urganch davlat universiteti").province(Province.XORAZM).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti Urganch filiali").province(Province.XORAZM).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Urganch davlat pedagogika instituti").province(Province.XORAZM).orgType(OrgType.DAVLAT).build());
        //Qashqadaryo
        organizationService.save(Organization.builder().name("Toshkent kimyo-texnologiya instituti Shahrisabz filiali").province(Province.QASHQADARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Qarshi davlat universiteti").province(Province.QASHQADARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Shahrisabz davlat pedagogika instituti").province(Province.QASHQADARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Qarshi muhandislik - iqtisodiyot instituti").province(Province.QASHQADARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent axborot texnologiyalari universiteti Qarshi filiali").province(Province.QASHQADARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("“TIQXMMI” Milliy tadqiqot universitetining Qarshi irrigatsiya va agrotexnologiyalar instituti").province(Province.QASHQADARYO).orgType(OrgType.DAVLAT).build());
        //Surxondaryo
        organizationService.save(Organization.builder().name("Termiz davlat universiteti").province(Province.SURXONDARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Denov tadbirkorlik va pedagogika instituti").province(Province.SURXONDARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Termiz davlat pedagogika instituti").province(Province.SURXONDARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Toshkent tibbiyot akademiyasi Termiz filiali").province(Province.SURXONDARYO).orgType(OrgType.DAVLAT).build());
        organizationService.save(Organization.builder().name("Termiz davlat muhandislik va agrotexnologiyalar universiteti").province(Province.SURXONDARYO).orgType(OrgType.DAVLAT).build());

        Role roleAdmin = new Role(RoleName.ROLE_ADMIN);
        Role roleUser = new Role(RoleName.ROLE_EMPLOYEE);
        roleService.saveAll(List.of(roleAdmin, roleUser));

        User employeeUser = User.builder()
                .email("nurzodbekmardiyev1306@gmail.com")
                .password("123")
                .roles(List.of(roleUser))
                .build();
        userService.save(employeeUser);

        Employee employee = Employee.builder()
                .user(employeeUser)
                .build();
        employeeService.save(employee);


        User adminUser = User.builder()
                .email("rasulloffoybek449733@gmail.com")
                .password("123")
                .roles(List.of(roleAdmin))
                .build();
        userService.save(adminUser);

        Admin admin = new Admin(adminUser);
        adminService.save(admin);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20); // Asosiy 20 ta parallel vazifa
        executor.setMaxPoolSize(50);  // Maksimal 50 ta parallel vazifa
        executor.setQueueCapacity(100); // Navbatdagi vazifalar sig'imi 100 ta
        executor.initialize();
        return executor;
    }

}
