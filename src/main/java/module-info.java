module mszalewicz.trygghet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.tomlj;
    requires java.sql;


    opens mszalewicz.trygghet to javafx.fxml;
    exports mszalewicz.trygghet;
}