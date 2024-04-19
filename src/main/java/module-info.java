module mszalewicz.trygghet {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.tomlj;


    opens mszalewicz.trygghet to javafx.fxml;
    exports mszalewicz.trygghet;
}