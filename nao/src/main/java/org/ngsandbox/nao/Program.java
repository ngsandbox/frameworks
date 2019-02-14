package org.ngsandbox.nao;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.DynamicObjectBuilder;
import com.aldebaran.qi.QiService;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.ALProxy;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALPhotoCapture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Program {

    public static String ROBOT_URL = "tcp://192.168.0.14:9559";

    public static void main(String... args) throws Exception {
        // Create a new application
        final Application application = new Application(args, ROBOT_URL);
        //AnyObject alRobotModel = application.session().service("ALRobotModel");
        CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
        }).thenAccept(aVoid -> {
            //takePhoto(application);
        }).thenAccept(aVoid -> {
            //advertise(application);
        }).thenAccept(aVoid -> {
            //moveTo(application);
            sayHelloToTheWorld(application);
        });

        //future.join();
        //sayHelloToTheWorld(application);
        //takePhoto(application);
        //advertise(application);
        //moveTo(application);
    }

    private static void sayHelloToTheWorld(Application application) {
        // Start your application
        application.start();
        try {
            // Create an ALTextToSpeech object and link it to your current session
            ALTextToSpeech tts = new ALTextToSpeech(application.session());
            tts.say("Привет! ");
            //tts.say("Vt! My name is ROBOT");
            //tts.say("Привет! ");
            for (int i = 500000; i > 0; i--) {
                // Make your robot say something
                tts.say("" + i);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error say hello", e);
        } finally {
            application.stop();
        }
    }

    private static void takePhoto(Application application) {
        // Start your application
        application.start();
        // Create an ALTextToSpeech object and link it to your current session
        try {
            ALPhotoCapture pc = new ALPhotoCapture(application.session());

            if (pc.ping()) {
                Integer cameraID = pc.getCameraID();
                String brokerName = pc.getBrokerName();
                Object takePicture = pc.takePicture("/home/nao", "image" + System.nanoTime() + ".png", true);
                System.out.print("returned: ");
                System.out.print(takePicture);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error take photo", e);
        } finally {
            application.stop();
        }
    }

    private static void moveTo(Application application) {
        ;
        // Start your application
        application.start();
        try {
            // Create an ALTextToSpeech object and link it to your current session
            Session session = application.session();
            ALMotion motion = new ALMotion(session);
            if (!motion.robotIsWakeUp()) {
                motion.wakeUp();
            }

            List<Float> robotPosition = motion.getRobotPosition(true);
            float x = 10, y = 10, theta = 10;
            if (robotPosition != null && !robotPosition.isEmpty()) {
                x = robotPosition.get(0);
                y = robotPosition.size() > 1 ? robotPosition.get(1) : y;
                theta = robotPosition.size() > 2 ? robotPosition.get(2) : theta;
            }

            //ALTextToSpeech tts = new ALTextToSpeech(session);
            //tts.say("Текущая позиция x: " + x + "; y " + y + "; theta: " + theta);
            Thread.sleep(1000);
            x *= 2;
            y *= 2;
            theta *= -1;
            //tts.say("Текущая позиция x: " + x + "; y " + y + "; theta: " + theta);
            Thread.sleep(1000);
            //motion.moveTo(2 * x, 2 * y, -theta);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error take photo", e);
        } finally {
            application.stop();
        }
    }

    private static void advertise(Application application) {
        try {
            // Create an instance of your service
            QiService service = new HelloService();

            // Create a DynamicObjectBuilder, that will render your service
            // compatible with other supported languages.
            DynamicObjectBuilder objectBuilder = new DynamicObjectBuilder();
            service.init(objectBuilder.object());

            // Advertise the greet method contained in your HelloService service.
            // You need to specify its signature.
            objectBuilder.advertiseMethod("greet::s(s)", service,
                    "Greets the caller");

            // Start your application
            application.start();

            // Retrieve the created session
            Session session = application.session();

            // Register your service in your session
            session.registerService("MyHelloService", objectBuilder.object());

            // Run your application
            application.run();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error advertise", e);
        } finally {
            application.stop();
        }

    }
}
