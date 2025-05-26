import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileIO {
    String filepath = "db/wiseSaying";
    int number;

    public String setJson(wiseSaying ws) {
        String json = "{\n" +
                "  \"id\": " + ws.getNum() + ",\n" +
                "  \"content\": \"" + ws.getWise() + "\",\n" +
                "  \"author\": \"" + ws.getAuthor() + "\"\n" +
                "}";
        return json;
    }

    boolean existFile(String path) {
        File file = new File(path);

        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    int getFileNum() {
        File file = new File(filepath + "/lastID.txt");

        if (!file.exists()) {
            String num = "0";

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(num);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String num = reader.readLine();
                number = Integer.parseInt(num);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return number;
    }

    int updateFileNum(int num) {
        File file = new File(filepath + "/lastID.txt");

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(Integer.toString(num));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return number;
    }

    void makeFileJson(wiseSaying ws) {
        String json = setJson(ws);
        File file = new File(filepath + "/" + ws.getNum() + ".json");

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    wiseSaying readFileJson(int n) {
        String path = filepath + "/" + n + ".json";

        // if (existFile(path))
        //     return;

        try {
            // 1. JSON 파일 읽기
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line.trim());
            }
            reader.close();

            String json = jsonBuilder.toString();

            int id = Integer.parseInt(json.replaceAll(".*\"id\"\\s*:\\s*(\\d+).*", "$1"));
            String content = json.replaceAll(".*\"content\"\\s*:\\s*\"(.*?)\".*", "$1");
            String author = json.replaceAll(".*\"author\"\\s*:\\s*\"(.*?)\".*", "$1");

            return new wiseSaying(id, content, author);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    void updateFileJson(int n, String content, String author) {
        String path = filepath + "/" + n + ".json";;

        try {
            // 1. JSON 파일 읽기
            BufferedReader reader = new BufferedReader(new FileReader(path));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line.trim());
            }
            reader.close();

            String json = jsonBuilder.toString();

            // 기존 값 파싱 (정규식 기반)
            int id = Integer.parseInt(json.replaceAll(".*\"id\"\\s*:\\s*(\\d+).*", "$1"));

            // 파일에 예쁘게 저장
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write("{\n");
            writer.write("  \"id\": " + id + ",\n");
            writer.write("  \"content\": \"" + content + "\",\n");
            writer.write("  \"author\": \"" + author + "\"\n");
            writer.write("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void deleteFileJson(int n) {
        String path = filepath + "/" + n + ".json";

        File file = new File(path);
        file.delete();
    }
    
    void mergeFile() {
        String outputFile = filepath + "/data.json";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write("[\n");

            boolean first = true;

            for (int i = 1; i <= getFileNum(); i++) {
                String filename = filepath + "/" + i + ".json";
                File file = new File(filename);

                if (!file.exists())
                    continue;

                StringBuilder jsonObject = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonObject.append(line).append("\n");
                    }
                }

                if (!first) {
                    writer.write(",\n");
                } else {
                    first = false;
                }

                writer.write(jsonObject.toString().trim());
            }

            writer.write("\n]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
