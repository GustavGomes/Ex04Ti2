package ti2ex4_artifact;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpeechToText {
    private static String YourSubscriptionKey = "ec45b084f31048c393bf0e711a5a3ec2";
    private static String YourServiceRegion = "brazilsouth";

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        SpeechConfig speechConfig = SpeechConfig.fromSubscription(YourSubscriptionKey, YourServiceRegion);
        speechConfig.setSpeechRecognitionLanguage("pt-BR");
        recognizeFromMicrophone(speechConfig);
    }

    public static void recognizeFromMicrophone(SpeechConfig speechConfig) throws InterruptedException, ExecutionException {
        //To recognize speech from an audio file, use `fromWavFileInput` instead of `fromDefaultMicrophoneInput`:
        //AudioConfig audioConfig = AudioConfig.fromWavFileInput("YourAudioFile.wav");
        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        try (SpeechRecognizer speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig)) {
			System.out.println("Fale com o microfone ligado:");
			Future<SpeechRecognitionResult> task = speechRecognizer.recognizeOnceAsync();
			SpeechRecognitionResult speechRecognitionResult = task.get();

			if (speechRecognitionResult.getReason() == ResultReason.RecognizedSpeech) {
			    System.out.println("RECONHECIDO: Texto=" + speechRecognitionResult.getText());
			}
			else if (speechRecognitionResult.getReason() == ResultReason.NoMatch) {
			    System.out.println("NOMATCH: Speech could not be recognized.");
			}
			else if (speechRecognitionResult.getReason() == ResultReason.Canceled) {
			    CancellationDetails cancellation = CancellationDetails.fromResult(speechRecognitionResult);
			    System.out.println("CANCELED: Reason=" + cancellation.getReason());

			    if (cancellation.getReason() == CancellationReason.Error) {
			        System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
			        System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
			        System.out.println("CANCELED: Did you set the speech resource key and region values?");
			    }
			}
		}

        System.exit(0);
    }
}