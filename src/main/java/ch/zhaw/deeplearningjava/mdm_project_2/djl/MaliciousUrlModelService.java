package ch.zhaw.deeplearningjava.mdm_project_2.djl;

import ai.djl.Model;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.EasyTrain;
import ai.djl.training.Trainer;
import ai.djl.training.TrainingConfig;
import ai.djl.training.dataset.RandomAccessDataset;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public class MaliciousUrlModelService {

    private static final Logger logger = LoggerFactory.getLogger(MaliciousUrlModelService.class);

    private static final int BATCH_SIZE = 128;
    private static final int EPOCH = 7;

    public static void main(String[] args) throws IOException, TranslateException {
        MaliciousUrlModel maliciousUrlModel = MaliciousUrlModel.getInstance();
        maliciousUrlModel.defineModel();
        Model model = maliciousUrlModel.getModel();

        logger.info("Loading Dataset");
        CSVDataset csvDataset = CSVDataset.builder().setSampling(BATCH_SIZE, true).build();
        RandomAccessDataset[] datasets = csvDataset.randomSplit(8, 2);

        TrainingConfig config =
                new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                        .addEvaluator(new Accuracy())
                        .addTrainingListeners(TrainingListener.Defaults.logging());

        try (Trainer trainer = model.newTrainer(config)) {
            trainer.initialize(CSVDataset.getInitializeShape());
            logger.info("Begin Training");
            EasyTrain.fit(trainer, EPOCH, datasets[0], datasets[1]);
        }

        model.setProperty("Epoch", String.valueOf(EPOCH));
        model.save(Paths.get("model"), "maliciousURLCNNModel");
    }
}
