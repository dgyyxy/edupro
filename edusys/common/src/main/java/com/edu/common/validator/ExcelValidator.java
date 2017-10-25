package com.edu.common.validator;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * 上传excel后缀验证
 * Created by Gary on 2017/4/14.
 */
public class ExcelValidator extends ValidatorHandler<CommonsMultipartFile> implements Validator<CommonsMultipartFile> {


    @Override
    public boolean validate(ValidatorContext context, CommonsMultipartFile commonsMultipartFile) {
        String realFileName = commonsMultipartFile.getOriginalFilename();
        String suffix = realFileName.substring(realFileName.indexOf(".")+1, realFileName.length());
        if(!suffix.equals("xls") && !suffix.equals("xlsx")){
            context.addError(ValidationError.create(String.format("上传文件只允许是Excel格式(xls或xlsx)"))
                    .setErrorCode(-1)
                    .setInvalidValue(commonsMultipartFile));
            return false;
        }

        return true;
    }
}
