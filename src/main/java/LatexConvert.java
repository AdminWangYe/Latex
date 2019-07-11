import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * @author ：yyWang
 * @date ：Created in 2019/7/5
 * @description： 将文本中 英文字母用LaTeX 的 $ 进行包裹
 */
public class LatexConvert {

    // 匹配 LaTeX 公式，
    public static final String LatexExpress = "\\$.*?\\$";
    // 匹配 ；类似∠1+∠2+∠3+∠4=100或者100°的表达式
    public static final String AngleExpress = "∠[0-9]+?\\+?∠[0-9]+?\\+?∠[0-9]+?\\+?∠[0-9]+?=\\d+°?";

    public static final String SegmentExpress="";

    // LaTeX 用下面的词进行替换，最后再转换
    public static final List<String> replaceLatex = new ArrayList<String>(Arrays.asList(
            "魑魅", "魍魉", "狰讙", "驳鯥", "蠃鱼", "孰湖", "穷奇", "伏羲琴", "冉遗鱼", "鵸鵌", "东皇钟", "昊天塔", "异兽狡", "玃如", "毕方",
            "豪彘", "鹿蜀", "狌狌", "羬羊", "虎蛟", "瞿如", "猾褢", "蛊雕", "狸力", "赤鱬", "灌灌", "猼訑", "盘古斧", "旋龟", "鸾鸟",
            "溪边", "文鳐", "白雉", "梼杌", "天之痕", "轩辕剑", "神农鼎", "炼妖壶", "昆仑镜", "崆峒", "乾坤鼎", "西王母", "洪荒", "盘古幡", "诛仙剑阵"
    ));

    public static void main(String[] args) {

        System.out.println(replaceLatex.size());
        HashSet<String> set = new HashSet<String>(LatexConvert.replaceLatex);
        System.out.println(set.size());

    }


}
