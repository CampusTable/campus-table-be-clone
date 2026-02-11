package shop.campustable.campustablebeclone.global.s3.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class FileUtil {

  public boolean isNullOrEmpty(MultipartFile file){
    return file == null || file.isEmpty() || file.getOriginalFilename() == null;
  }

}
