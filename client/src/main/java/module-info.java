module client {

    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.desktop;
    requires org.junit.jupiter.api;
    requires javafx.media;
    requires jfoenix;

    opens model to javafx.fxml;
    opens view.menus to javafx.fxml;
    exports view.menus;
    exports model;

}