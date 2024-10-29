package jon.basicspring;

import ai.onnxruntime.OnnxTensor;
import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.io.FileInputStream;
import java.io.File;

@Controller
public class BankController {

    @GetMapping("/bank")
    public String showForm(Model model) {
        return "bank";
    }

    @PostMapping("/bank")
    public String submitForm(
            @RequestParam("number1") double number1,
            @RequestParam("number2") double number2,
            @RequestParam("number3") double number3,
            @RequestParam("number4") double number4,
            @RequestParam("number5") double number5,
            @RequestParam("number6") double number6,
            @RequestParam("number7") double number7,
            @RequestParam("number8") double number8,
            @RequestParam("number9") double number9,
            @RequestParam("number10") double number10,
            @RequestParam("number11") double number11,
            @RequestParam("number12") double number12,
            @RequestParam("number13") double number13,
            Model model) {

        model.addAttribute("number1", number1);
        model.addAttribute("number2", number2);
        model.addAttribute("number3", number3);
        model.addAttribute("number4", number4);
        model.addAttribute("number5", number5);
        model.addAttribute("number6", number6);
        model.addAttribute("number7", number7);
        model.addAttribute("number8", number8);
        model.addAttribute("number9", number9);
        model.addAttribute("number10", number10);
        model.addAttribute("number11", number11);
        model.addAttribute("number12", number12);
        model.addAttribute("number13", number13);
        model.addAttribute("bankpredic", runInference(number1, number2, number3, number4, number5, number6, number7, number8, number9, number10, number11, number12, number13));

        return "bankresult";
    }

    String runInference(double... numbers) {
        String modelPath = "/bankModel.onnx";
        String scalarPath = "scaler.pkl";
        try (InputStream is = ANDController.class.getResourceAsStream(modelPath);
             OrtEnvironment env = OrtEnvironment.getEnvironment();
             InputStream scalarStream = new ClassPathResource(scalarPath).getInputStream();
             ObjectInputStream ois = new ObjectInputStream(scalarStream)) {

            // Load the scalar
            StandardScaler scalar = (StandardScaler) ois.readObject();

            // Transform the input data
            float[][] inputData = new float[1][numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                inputData[0][i] = (float) numbers[i];
            }
            inputData = scalar.transform(inputData);

            // Run inference
            assert is != null;
            OrtSession session = env.createSession(is.readAllBytes());
            OnnxTensor inputTensor = OnnxTensor.createTensor(env, inputData);
            OrtSession.Result result = session.run(Collections.singletonMap("inputs", inputTensor));

            float[][] output = (float[][]) result.get(0).getValue();
            return "" + Arrays.toString(output[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}