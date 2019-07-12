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
        Pattern pattern = Pattern.compile(ChinanesStr);
        String[] strs = pattern.split(str.trim());
        List<String> strset = new ArrayList<>(Arrays.asList(strs));
        // 删除一些标点符号，统一格式
        strset.removeAll(deleStr);
        // 遍历集合并进行替换
        for (String string : strset) {
            dealStr(string);
        }

        // 按照文本中的实体，替换首次匹配到的实体
        for (Map.Entry<String, String> entry : keymap.entrySet()) {
            str = str.replaceFirst(Pattern.quote(entry.getKey()), Matcher.quoteReplacement(entry.getValue()));
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
                String newStr = str;
                if (!str.contains("$") && !str.equals("")) {
                    if (str.contains("=")) {
                        newStr = "$" + str + "$";
                    } else if (str.contains("△")) {
                        newStr = str.replaceAll("△([A-Z]+)", "\\$$1\\$");
                    } else if (str.contains("∠")) {
                        newStr = str.replaceAll("∠([A-z]+)", "\\$$1\\$");
                    } else if (!patternNum(str)) {
                        newStr = "$" + str + "$";
                    }
                }
                keymap.put(str, newStr);
            }
        } else {
            String replaceStr = string;
            if (!string.contains("$") && !string.equals("")) {
                replaceStr = "$" + string + "$";
            }
            keymap.put(string, replaceStr);
        }

    }

    public static void main(String[] args) {

        LatexConvert latexConvert = new LatexConvert();
        String str = "如图,矩形 $OABC$ 的两边 $OA$ 、 $OC$ 分别在x轴、y轴的正半轴上, $OA=4$ , $OC=2$ , $G$ 为矩形对角线的交点,经过点 $G$ 的双曲线 $y=\\frac{k}{x}$ 与 $BC$ 相交于点 $M$ ,则 $CM:MB=$";
        System.out.println("原字符串：" + str);
        System.out.println(latexConvert.getPattern(str));


    }


}
