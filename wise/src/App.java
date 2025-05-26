import java.util.*;

public class App {
    Scanner sc = new Scanner(System.in);
    FileIO fi = new FileIO(); // file i/o
    int num = fi.getFileNum();
    String filepath = "db/wiseSaying";

    List<wiseSaying> wslist = new ArrayList<>();

    void run() {
        wslist.add(new wiseSaying(num, null, null)); // 쓰레기 값
        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String s = sc.next();

            if (s.equals("종료")) {
                break;
            } else if (s.equals("등록")) {
                register();
            } else if (s.equals("목록")) {
                getlist();
            } else if (s.equals("빌드")) {
                build();
            } else if (s.substring(0, 6).equals("삭제?id=")) {
                delete(Integer.parseInt(s.substring(6)));
            } else if (s.substring(0, 6).equals("수정?id=")) {
                update(Integer.parseInt(s.substring(6)));
            } else {
                System.out.println("해당하지 않는 기능이거나 올바르지 않는 입력입니다.");
            }
        }
    }

    void register() {
        num++; // 번호 증가

        System.out.print("명언 : ");
        String wise = sc.next();
        System.out.print("작가 : ");
        String author = sc.next();

        wiseSaying ws = new wiseSaying(num, wise, author);
        fi.makeFileJson(ws);

        fi.updateFileNum(num);
        System.out.println(num + "번 명언이 등록되었습니다.");
    }

    void getlist() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        
        for (int i = fi.getFileNum(); i > 0; i--) {
            if (!fi.existFile(filepath + "/" + i + ".json"))
                continue;

            wiseSaying ws = fi.readFileJson(i);

            System.out.println(ws.getNum() + " / " + ws.getAuthor() + " / " + ws.getWise());
        }
    }

    void delete(int n) {
        String path = filepath + "/" + n + ".json";

        if (fi.existFile(path)) {
            fi.deleteFileJson(n);
            System.out.println(n + "번 명언이 삭제되었습니다.");
        } else {
            System.out.println(n + "번 명언이 존재하지 않습니다.");
        }
    }

    void update(int n) {
        String path = filepath + "/" + n + ".json";

        if (fi.existFile(path)) {
            wiseSaying ws = fi.readFileJson(n);

            System.out.println("명언(기존) : " + ws.getWise());
            System.out.print("명언 : ");
            String content = sc.next();

            System.out.println("작가(기존) : " + ws.getAuthor());
            System.out.print("작가 : ");
            String author = sc.next();

            fi.updateFileJson(n, content, author);
        } else {
            System.out.println(n + "번 명언이 존재하지 않습니다.");
        }
    }

    void build() {
        fi.mergeFile();
        System.out.println("data.json 파일의 내용이 갱신되었습니다.");
    }
}
