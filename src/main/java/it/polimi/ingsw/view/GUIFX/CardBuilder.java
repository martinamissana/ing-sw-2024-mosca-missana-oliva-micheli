package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.RMIView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CardBuilder {
    @FXML
    ImageView cardImage = new ImageView();
    @FXML
    private final Card card;
    private String PATH = "/images/";

    public CardBuilder(Card card) {
        this.card = card;
        initialize();
    }


    public void initialize() {
        String pathgenerale = getClass().getResource(PATH).toString();
        if (card.getSide() == CardSide.BACK) {
            if (card instanceof ResourceCard)
                pathgenerale = pathgenerale + "/backs/" + (card.getCardID() - 1) / 10 + ".png";
            else if (card instanceof StarterCard) pathgenerale = pathgenerale + "/backs/" + card.getCardID() + ".png";
        }
        else {
            if (!(card instanceof CardBlock)) pathgenerale = pathgenerale + "/fronts/" + card.getCardID() + ".png";
        }
        Image image = new Image(pathgenerale);
        cardImage.setFitWidth(100);
        cardImage.setFitHeight(66.66);
        cardImage.setImage(image);
    }

    public ImageView getCardImage() {
        return cardImage;
    }
}
