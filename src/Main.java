import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parse(args);
        String mode = argumentParser.getMode();
        Path outPath = argumentParser.getOutPath();
        long limitBlockSize = argumentParser.getLimitBlockSize();
        List<Path> inFileNames = argumentParser.getInFileNames();


        FileProcessing fileProcessing = new FileProcessing();
        fileProcessing.mergeSort(mode, outPath, inFileNames, limitBlockSize);
    }
}
