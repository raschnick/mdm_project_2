package ch.zhaw.deeplearningjava.mdm_project_2.djl;

import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.transform.Resize;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Inference {

    Predictor<Image, Classifications> predictor;
    Translator<Image, Classifications> translator = ImageClassificationTranslator.builder()
            .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
            .addTransform(new ToTensor())
            .optApplySoftmax(true)
            .build();

    public Inference() throws MalformedModelException, IOException {
        Model model = Models.getModel();
        Path modelDir = Paths.get("models");
        model.load(modelDir, Models.MODEL_NAME);

        this.predictor = model.newPredictor(translator);
    }

    public static void main(String[] args) throws ModelException, TranslateException, IOException {
        Path modelDir = Paths.get("models");

        String imageFilePath;
        if (args.length == 0) {
            imageFilePath = "src/main/resources/ut-zap50k-images-square/Sandals/Heel/Annie/7350693.3.jpg";
        } else {
            imageFilePath = args[0];
        }

        Image img = ImageFactory.getInstance().fromFile(Paths.get(imageFilePath));

        try (Model model = Models.getModel()) {
            model.load(modelDir, Models.MODEL_NAME);

            Translator<Image, Classifications> translator =
                    ImageClassificationTranslator.builder()
                            .addTransform(new Resize(Models.IMAGE_WIDTH, Models.IMAGE_HEIGHT))
                            .addTransform(new ToTensor())
                            .optApplySoftmax(true)
                            .build();

            try (Predictor<Image, Classifications> predictor = model.newPredictor(translator)) {
                Classifications predictResult = predictor.predict(img);
                System.out.println(predictResult);
            }
        }
    }

    public Classifications predict(byte[] image) throws ModelException, TranslateException, IOException {
        InputStream is = new ByteArrayInputStream(image);
        BufferedImage bi = ImageIO.read(is);
        Image img = ImageFactory.getInstance().fromImage(bi);
        return this.predictor.predict(img);
    }

}