package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.card.*;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


/**
 * Class CardBuilder
 * used to generate image of the card
 */
public class CardBuilder {
    @FXML
    ImageView cardImage = new ImageView();
    @FXML
    private final Card card;
    private String PATH = "/images";

    /**
     * Class constructor
     * @param card the card
     */
    public CardBuilder(Card card) {
        this.card = card;
        initialize();
    }


    /**
     * used to find the path of the image and load it
     */
    public void initialize() {
        String generalPath = getClass().getResource(PATH).toExternalForm();
        if (card.getSide() == CardSide.BACK) {
            if (card instanceof ResourceCard)
                generalPath = generalPath + "/backs/" + (card.getCardID() - 1) / 10 + ".png";
            else if (card instanceof StarterCard) generalPath = generalPath + "/backs/" + card.getCardID() + ".png";
        }
        else {
            if (!(card instanceof CardBlock)) generalPath = generalPath + "/fronts/" + card.getCardID() + ".png";
        }
        Image image = new Image(generalPath);
        cardImage.setFitWidth(100);
        cardImage.setFitHeight(66.66);
        cardImage.setImage(image);
    }

    /**
     * @return ImageView
     */
    public ImageView getCardImage() {
        return cardImage;
    }
}
