module mszalewicz.trygghet {
    requires javafx.controls;
    requires javafx.fxml;


    opens mszalewicz.trygghet to javafx.fxml;
    exports mszalewicz.trygghet;
}