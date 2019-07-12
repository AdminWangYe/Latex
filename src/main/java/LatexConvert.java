import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：yyWang
 * @date ：Created in 2019/7/5
 * @description： 将文本中 英文字母用LaTeX 的 $ 进行包裹
 * 思路：
 * 1. 先用正则进行中文匹配，然后使用中文进行分割
 */
public class LatexConvert {

    // 保存替换后的字符串
    private Map<String, String> keymap = new LinkedHashMap<>();
    // 将转LaTeX化的实体进行替换成 replaceLatex 集合中的字符
    private Map<String, String> replaceMap = new LinkedHashMap<>();
    // 从替换的元素中任选一个进行替换
    private static Random random = new Random();
    // 匹配 LaTeX 公式，
    public static final String LatexExpress = "\\$.*?\\$";
    // 匹配 ；类似∠1+∠2+∠3+∠4=100或者100°的表达式
    public static final String AngleExpress = "∠[0-9]+?\\+?∠[0-9]+?\\+?∠[0-9]+?\\+?∠[0-9]+?=\\d+°?";

    private static final String ChinanesStr = "[\\u4e00-\\u9fa5_]+:?";

    public static final String SegmentExpress = "";

    // LaTeX 用下面的词进行替换，最后再转换
    private static final List<String> replaceLatex = new ArrayList<String>(Arrays.asList(
            "魑魅", "魍魉", "狰讙", "驳鯥", "蠃鱼", "孰湖", "穷奇", "伏羲琴", "冉遗鱼", "鵸鵌", "东皇钟", "昊天塔", "异兽狡", "玃如", "毕方",
            "豪彘", "鹿蜀", "狌狌", "羬羊", "虎蛟", "瞿如", "猾褢", "蛊雕", "狸力", "赤鱬", "灌灌", "猼訑", "盘古斧", "旋龟", "鸾鸟",
            "溪边", "文鳐", "白雉", "梼杌", "天之痕", "轩辕剑", "神农鼎", "炼妖壶", "昆仑镜", "崆峒", "乾坤鼎", "西王母", "洪荒", "盘古幡", "诛仙剑阵"
    ));

    private List<String> deleStr = new ArrayList<>(Arrays.asList(
            "", "(", ",", "“", "”", ")", "()"
    ));


    /**
     * 文本中的实体 LaTeX 化，
     *
     * @param str 待处理的文本
     * @return 返回处理后的文本
     */
    private String getPattern(String str) {
        // 每次处理一个新的文本都需要将 map 集合里的数据清空
        keymap.clear();
        replaceMap.clear();
        Pattern pattern = Pattern.compile(ChinanesStr);
        String[] strs = pattern.split(str.trim());
        List<String> strset = new ArrayList<>(Arrays.asList(strs));
        // 删除一些标点符号，统一格式
        strset.removeAll(deleStr);
        // 遍历集合并进行替换
        for (String string : strset) {
            dealStr(string);
        }

        // 将已经使用的字符移除掉
        List<String> stringList = replaceLatex;

        // 按照文本中的实体，替换首次匹配到的实体
        for (Map.Entry<String, String> entry : keymap.entrySet()) {
            str = str.replaceFirst(Pattern.quote(entry.getKey()), Matcher.quoteReplacement(entry.getValue()));
            // 虽然按照首次匹配的正则进行替换，但是还会出现后面覆盖前面实体LaTeX化的内容，这时可以将已经 LaTeX化的实体利用其它字符进行替换。
            str = replaceStr(str, stringList);
        }

        str = replaceEndStr(str);

        return str;
    }

    /**
     * 将用其他字符替换的 LaTeX化的实体，进行还原，成原字符串
     *
     * @param str 需要替换的字符串
     * @return 返回替换后的字符串
     */
    private String replaceEndStr(String str) {
        for (Map.Entry<String, String> entry : replaceMap.entrySet()) {
            str = str.replace(entry.getKey(), entry.getValue());
        }
        return str;
    }

    /**
     * 将已经转成 LaTeX 的实体进行其他字符替代以免被覆盖
     *
     * @param str        需要处理的字符串
     * @param stringList 保留关键字的集合
     * @return
     */
    private String replaceStr(String str, List<String> stringList) {
        String reg = "(\\$.*?\\$)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            // 从list 集合中取出一个字符进行替换。关键字
            String key = stringList.get(random.nextInt(stringList.size()));
            // 替换一个
            str = str.replace(matcher.group(1), key);
            stringList.remove(key);
            replaceMap.put(key, matcher.group(1));
        }
        return str;
    }


    /**
     * 判断字符串是否只包含数字，如果是数字就不需要进行$ 包裹
     *
     * @param str 待匹配的字符串
     * @return 返回匹配的结果
     */
    private boolean patternNum(String str) {
        String regex = "[-0-9_]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 对list集合里的字符串进行处理，并分割成单个字符。进行实体 LaTeX 化
     * 处理后的结果保存在keymap 全局集合中返回处理的 map 集合，就是处理过的字符串，关键字是原字符串，对应的值是替换后的 LaTeX实体
     *
     * @param string 需要处理的字符串
     */
    private void dealStr(String string) {
        // 如果是坐标的话就按照坐标进行切分，否则就按照逗号或者顿号进行切割
        String[] old = string.split("[A-z]+\\([A-z0-9],[A-z0-9]\\)|、|,");
        if (old.length > 1) {
            for (String str : old) {
                // 去掉字符串的前后空格的影响
                str = str.trim();
                // 如果包含字符串包含$，说明该字符串是LaTeX格式，不需要进行处理
//                String newStr = newStrReplace(str);
//                keymap.put(str, newStr);
                dealStr(str);
            }
        } else {
            String replaceStr = newStrReplace(string);
            keymap.put(string, replaceStr);
        }

    }

    /**
     * 单个字符串处理，是否添加 $,也就是添加匹配规则。
     *
     * @param str 需要处理的字符串
     * @return 返回添加 $ 后的字符串，也就是返回 LaTeX 化的实体。
     */
    private String newStrReplace(String str) {
        if (!str.contains("$") && !str.equals("")) {
            if (str.contains("=")) {
                str = "$" + str + "$";
            } else if (str.contains("△") || str.contains("∥")) {
                str = str.replaceAll("[△∥]?([A-Z']+)", "\\$$1\\$");
            } else if (str.contains("∠")) {
                str = str.replaceAll("∠([A-z]+)", "\\$$1\\$");
            } else if (str.contains("⊥")) {
                str = str.replaceAll("([A-Z]+)", "\\$$1\\$");
            } else if (!patternNum(str)) {
                str = "$" + str + "$";
            }
        }
        return str;
    }

    public static void main(String[] args) {

        LatexConvert latexConvert = new LatexConvert();
        String str = "四边形ABCD的对角线AC,BD交于点O,已知O是AC的中点,AE=CF,DF∥BE, 证明:△BOE≌△DOF, 若OD=1/2AC,则四边形ABCD是什么特殊四边形,请证明你的结论";
        System.out.println("原字符串：" + str);
        System.out.println(latexConvert.getPattern(str));


    }


}
