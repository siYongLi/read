import net.ninemm.base.gencode.controller.AppControllerGenerator;
import net.ninemm.base.gencode.model.AppModelGenerator;
import net.ninemm.base.gencode.service.AppServiceGenerator;
import net.ninemm.base.gencode.serviceimpl.AppServiceImplGenerator;

/**
 * service impl 代码生成
 * @author Eric
 * @date 2018-06-27 16:06
 */
public class GenCode {
    public static void main(String[] args) {
        AppModelGenerator.doGenerate();
        AppServiceGenerator.doGenerate();
        AppServiceImplGenerator.doGenerate();
        AppControllerGenerator.doGenerate();
    }
}
