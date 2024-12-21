package sovokguard.protect;

import me.sertyo.j2c.J2c;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@J2c
public class ApiContacts {
/*

         ApiContacts.uid получение юида
         ApiContacts.role получение роли
         ApiContacts.username получение никнейма

         нативку юзать надо в таком формате

         @J2c
         public(private и тд) (может быть class или void) {
         тут типо код
         }

         Если не надо накладывать нативную обфускацию

         @NotNative
         public(private и тд) (может быть class или void) {
         тут типо код
         }

         самое главное наложить на ваш основной класс клиента нативку aka @J2c
         а в метод инициализации написать ApiContacts.start();
         на этом все! самое главное ничего не менять!!!!

         так же вам надо добавить 2 библиотеки (gradle)

         Чаще всего и так есть
         implementation 'org.json:json:20210307'

         это надо обязательно
         implementation 'org.apache.httpcomponents:httpclient:4.5.13'

         если у вас maven то просто перепишите под maven если у вас все либки в папке lib/libs/libraries(и тд)
         вам надо скачать отдельно библиотеку и поместить в папку на этом все!!

*/
    public static final String NAME = "Wiksi"; // CLIENT NAME
    public static final File MAIN_DIRECTORY = new File("C:/" + NAME); //DIRECTORY TO CLIENT
    public static final String SESSION_FILE_NAME = MAIN_DIRECTORY + "/token"; //TOKEN PATH не трогать!!!
    public static final String SERVER_URL = "http://127.0.0.1:8000/api/v1/"; //API URL
    public static String username; //USERNAME
    public static int uid; //UID
    public static String role; //ROLE
    public static String token; //TOKEN не трогать!!!
    public static void start() {
        /*

        если вы делаете код в идеи то просто закоментируйте этот код и напишите

            username = "Ваш юзер";
            uid = 1;
            role = "Developer";

            при релизе удалите

            username = "Ваш юзер";
            uid = 1;
            role = "Developer";

            и раскоминтируйте код ниже
         */
        Path tokenFilePath = Path.of(SESSION_FILE_NAME);
        try {
            token = Files.readString(tokenFilePath);
        } catch (IOException e) {
            System.err.println("SOSI HUY " + e.getMessage());
            System.exit(-1);
        }
        Util.getinfo();
        Util.updateinformation();
        if (username == null)
            Crasher.crash();

        if (uid == 0)
            Crasher.crash();
        if (role == null)
            Crasher.crash();
        if (!role.equals("CEO")) {
            if (!System.getProperty("java.runtime.version").equals("21-AutistLeak-adhoc.Sertyo.openjdk")) {
                Crasher.crash();
            }
            if (!System.getProperty("java.version").equals("21-AutistLeak")) {
                Crasher.crash();
            }
        }
    }

}
