package org.frameworks.nao;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

public class ReactToEvents {

    private static ALTextToSpeech globalTts;

    public static void main(String[] args) throws Exception {

        // Create a new application
        Application application = new Application(args, Program.ROBOT_URL);
        // Start your application
        application.start();
        System.out.println("Successfully connected to the robot");
        // Subscribe to selected ALMemory events
//        ReactToEvents reactor = new ReactToEvents();
        Session session = application.session();
//        reactor.run(application.session());
        // Run your application
        try (NaoSubscriber subscriber = new NaoSubscriber(session)) {
            System.out.println("Subscribed to FrontTactilTouched and RearTactilTouched.");
            final ALTextToSpeech tts = new ALTextToSpeech(session);
            globalTts = tts;
/*
    HANDLEFTBACKTOUCHED("HandLeftBackTouched"),
    HANDLEFTLEFTTOUCHED("HandLeftLeftTouched"),
    HANDLEFTRIGHTTOUCHED("HandLeftRightTouched"),
    HANDRIGHTBACKTOUCHED("HandRightBackTouched"),
    HANDRIGHTLEFTTOUCHED("HandRightLeftTouched"),
    HANDRIGHTRIGHTTOUCHED("HandRightRightTouched"),

*
* */
            subscriber.subscribe(NaoEvent.HANDLEFTRIGHTTOUCHED, arg0 -> eventReactor(arg0, "HAND LEFT RIGHT TOUCHED", "Левая правая!") );
            subscriber.subscribe(NaoEvent.HANDRIGHTBACKTOUCHED, arg0 -> eventReactor(arg0, "HAND RIGHT BACK TOUCHED", "Правая задняя!") );
            subscriber.subscribe(NaoEvent.HANDRIGHTLEFTTOUCHED, arg0 -> eventReactor(arg0, "HAND RIGHT LEFT TOUCHED", "Правая левая!") );
            subscriber.subscribe(NaoEvent.HANDRIGHTRIGHTTOUCHED, arg0 -> eventReactor(arg0, "HAND RIGHT RIGHT TOUCHED", "Правая правая!") );
            subscriber.subscribe(NaoEvent.ROBOTISWAKEUP, arg0 -> eventReactor(arg0, "ROBOT IS WAKEUP", "Я проснулся!") );
            subscriber.subscribe(NaoEvent.SOUNDDETECTED, arg0 -> eventReactor(arg0, "sound  detected", "Зарегистрировано обращение!") );
            subscriber.subscribe(NaoEvent.FACEDETECTED, arg0 -> eventReactor(arg0, "face detected", "Лицо! Лицо!") );
            subscriber.subscribe(NaoEvent.FRONTTACTILTOUCHED, arg0 -> eventReactor(arg0, "front tactil touched", "Ай") );
            subscriber.subscribe(NaoEvent.REARTACTILTOUCHED, arg0 -> eventReactor(arg0, "react tactil touched", "Стоп") );
            application.run();
        }
    }

    private static void eventReactor(Object arg, String log, String say) {
        System.out.print(log + " ");
        System.out.println(arg);
        try {
            globalTts.say(say);
        } catch (CallError | InterruptedException callError) {
            callError.printStackTrace();
        }
    }

//    private ALMemory memory;
//    private ALTextToSpeech tts;
//    private long frontTactilSubscriptionId;
//
//    public void run(Session session) throws Exception {
//
//        memory = new ALMemory(session);
//        tts = new ALTextToSpeech(session);
//        frontTactilSubscriptionId = 0;
//
//        // Subscribe to FrontTactilTouched event,
//        // create an EventCallback expecting a Float.
//        frontTactilSubscriptionId = memory.subscribeToEvent(
//                NaoEvent.FRONTTACTILTOUCHED.getEvent(), (EventCallback<Float>) arg0 -> {
//                    // 1 means the sensor has been pressed
//                    if (arg0 > 0) {
//                        tts.say("ouch!");
//                    }
//                });
//        // Subscribe to RearTactilTouched event,
//        // create an EventCallback expecting a Float.
//        memory.subscribeToEvent("RearTactilTouched",
//                (EventCallback<Float>) arg0 -> {
//                    if (arg0 > 0) {
//                        if (frontTactilSubscriptionId > 0) {
//                            tts.say("I'll no longer say ouch");
//                            // Unsubscribing from FrontTactilTouched event
//                            memory.unsubscribeToEvent(frontTactilSubscriptionId);
//                        }
//                    }
//                });
//    }

}