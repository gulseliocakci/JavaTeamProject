package org.example;
public class Main {
    public static void main(String[] args) {
        GUI gui=new GUI();
        gui.GUI();
        EmailSender.sendPlainTextEmail("gocakci@gmail.com", gui.email,
                "Email örneği",
                "Selam Jakarta",
                true);
        

    }
}

