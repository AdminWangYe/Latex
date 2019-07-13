package latex;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ：yyWang
 * @date ：Created in 2019/7/5
 * @description： 将文本中 英文字母用LaTeX 的 $ 进行包裹
 * 思路：
 * 1. 先用正则进行中文匹配，然后使用中文进行分割
 * 2. 处理一些无用的字符，一些标点符号。
 * 3. 处理需要LaTeX化的实体，并将替换的结果保存到list 集合中。
 * 4. 对应文本根据list保存的内容进行替换，但是每个实体LaTeX化后都需要用其他字符代替。以免后面的结果覆盖当前内容
 * 5. 将用其他字符替换后文本恢复LaTeX化实体，处理完成，返回结果
 */
public class LatexConvert {

    // 保存替换后的字符串,可以保存重复的键值对
    private List<Map<String, String>> keymap = new ArrayList<>();
    // 将转LaTeX化的实体进行替换成 replaceLatex 集合中的字符
    private Map<String, String> replaceMap = new LinkedHashMap<>();
    // 从替换的元素中任选一个进行替换
    private static Random random = new Random();

    //    匹配中文字符
    private static final String ChinanesStr = "[\\u4e00-\\u9fa5_]+:?";


    //    需要删除的符号
    private ArrayList deleStr = new ArrayList<>(Arrays.asList(
            "", "(", ",", "“", "”", ")", "()", "（", "），", "，", "\"", "。", "、", "："
    ));


    /**
     * 文本中的实体 LaTeX 化，暴露接口给外界访问的入口
     *
     * @param str 待处理的文本
     * @return 返回处理后的文本
     */
    public String getPattern(String str) {
        // 每次处理一个新的文本都需要将 map 集合里的数据清空
        keymap.clear();
        replaceMap.clear();
        //  步骤1. 按照中文进行切分
        Pattern pattern = Pattern.compile(ChinanesStr);
        String[] strs = pattern.split(str.trim());
        List<String> strset = new ArrayList<>(Arrays.asList(strs));

        // 步骤2.删除一些标点符号，统一格式
        strset.removeAll(deleStr);

        // 步骤3.处理按照中文切分后的字符串，并按照格式再次切分，切割成只有单个字符或者实体
        for (String string : strset) {
            dealStr(string);
        }

        // LaTeX 用下面的词进行替换，最后再转换
        List<String> stringList = new ArrayList<>(Arrays.asList(
                "魑魅", "魍魉", "狰讙", "驳鯥", "蠃鱼", "孰湖", "穷奇", "伏羲琴", "冉遗鱼", "鵸鵌", "东皇钟", "昊天塔", "异兽狡", "玃如", "毕方",
                "豪彘", "鹿蜀", "狌狌", "羬羊", "虎蛟", "瞿如", "猾褢", "蛊雕", "狸力", "赤鱬", "灌灌", "猼訑", "盘古斧", "旋龟", "鸾鸟",
                "溪边", "文鳐", "白雉", "梼杌", "天之痕", "轩辕剑", "神农鼎", "炼妖壶", "昆仑镜", "崆峒", "乾坤鼎", "西王母", "洪荒", "盘古幡", "诛仙剑阵",
                "貝筆", "罷備", "畢邊", "參倉", "産長", "芻從", "達帶", "動斷", "樂離", "劉龍", "婁盧", "馬買", "門黽", "難鳥", "聶寜",
                "齊豈", "氣遷", "僉喬", "親窮", "嗇殺", "審聖", "師時", "夀屬", "雙肅", "嵗孫", "萬為", "韋烏", "獻鄉", "寫尋", "亞嚴",
                "厭堯", "業頁", "義兿", "陰隱", "猶魚", "與雲", "鄭執", "質專", "標錶", "彆蔔", "擔膽", "導燈", "鄧敵", "糴遞", "點澱"
        ));

        // 步骤4.按照文本中的实体，替换首次匹配到的实体
        for (Map<String, String> entity : keymap) {
            for (Map.Entry<String, String> edit : entity.entrySet()) {
                str = str.replaceFirst(Pattern.quote(edit.getKey()), Matcher.quoteReplacement(edit.getValue()));
                // 虽然按照首次匹配的正则进行替换，但是还会出现后面覆盖前面实体LaTeX化的内容，这时可以将已经 LaTeX化的实体利用其它字符进行替换。
                str = replaceStr(str, stringList);
            }

        }


        // 替换成原字符串
        str = replaceEndStr(str);

        return str;
    }

    /**
     * 步骤5 将用其他字符替换的 LaTeX化的实体，进行还原，成原字符串
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
     * 步骤4.将已经转成 LaTeX 的实体进行其他字符替代以免被覆盖
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
     * 判断该字符串是否只包含数字或者是坐标形式（比如A（2,3))，如果是数字就不需要进行$ 包裹
     * 如果不包含数字就返回真，
     *
     * @param str 待匹配的字符串
     * @return 返回匹配的结果
     */
    private boolean patternNum(String str) {
        String regex = "[^-0-9_°]+|[A-z]?\\([A-z0-9∞]+.*?\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 步骤3
     * 对list集合里的字符串进行处理，并分割成单个字符。进行实体 LaTeX 化
     * 处理后的结果保存在keymap 全局集合中返回处理的 list 集合，就是处理过的字符串，关键字是原字符串，对应的值是替换后的
     * LaTeX实体，再将这个map 放进keymap list 集合中，因为list集合允许元素重复。当一个题目中有多个重复的实体，后面进行
     * 处理时就会被替换。文本中有多少个需要转化的实体，list就有多少个元素，但是用hashmap 进行存储时元素个数就会小于这个值。
     *
     * @param string 需要处理的字符串
     */
    private void dealStr(String string) {
        // 如果是坐标的话就按照坐标进行切分，否则就按照逗号或者顿号进行切割
        // ，|[A-z]?\([A-z0-9∞]+.*?\)|、|,
        String[] old = string.split("[，、。；：]");
        if (old.length > 1) {
            for (String str : old) {
                // 去掉字符串的前后空格的影响
                str = str.trim();
                // 递归调用进行 实体LaTeX化
                dealStr(str);
            }
        } else if (!old.equals("") && old.length != 0) {
            String replaceStr = newStrReplace(old[0]);
            Map<String, String> keyValue = new HashMap<>();
            keyValue.put(old[0], replaceStr);
            // 将原实体添加到 map集合中，并保存对应的 键值对，key是原字符串，value是 LaTeX化的实体。然后将这个map 添加到list 集合中。
            keymap.add(keyValue);
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
            } else if (str.contains("△") || str.contains("∥") || str.contains("⊥") || str.contains("∠")) {
                str = str.replaceAll("([A-z']+)", "\\$$1\\$");
            } else if (patternNum(str)) {
                str = "$" + str + "$";
            }
        }
        return str;
    }

}
