package ce.daegu.ac.kr.aStartrip.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

@Service
@Slf4j
public class FileService {
    private final Path fileLocation; //파일이 저장될 위치

    public FileService(@Value("${file.location}") String dirName) {
        fileLocation = Paths.get(dirName).toAbsolutePath().normalize();
        try {
            log.debug("fileLocation:{}, dirName={}", fileLocation, dirName);
            Files.createDirectories(fileLocation);
        } catch (Exception e) {
            throw new RuntimeException("파일 저장 디렉토리 생성 실패함");
        }
    }

    public String saveFile(MultipartFile file, Long cardId) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getExtension(fileName);

        //같은 cardId 이전 업로드 확장자가 다를 경우 파일이 두개가 될 수 있음으로 제거 시도
        try {
            String search = cardId + ".*";
            DirectoryStream<Path> stream = Files.newDirectoryStream(fileLocation, search);
            for (Path entry : stream) {
                Files.delete(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //저장 시작 카드id.확장자
            Path targetLocation = fileLocation.resolve(String.valueOf(cardId) + '.' + extension);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Resource loadFile(Long cardId) {
        try {
            String search = cardId + ".*";
            DirectoryStream<Path> stream = Files.newDirectoryStream(fileLocation, search);
            for (Path entry : stream) {
                Path filePath = fileLocation.resolve(entry.getFileName());
                log.debug("파일 찾음: {}", filePath.toUri());
                return new UrlResource(filePath.toUri());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getExtension(String filename) {
        // 파일 이름에 점이 있는지 확인하고, 마지막 점의 위치를 찾습니다.
        int dotIndex = filename.lastIndexOf('.');

        // 점이 없거나 파일 이름의 마지막에 위치하는 경우 빈 문자열을 반환합니다.
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return "";
        }

        // 마지막 점 이후의 문자열(확장자)을 반환합니다.
        return filename.substring(dotIndex + 1);
    }

    public String getFilenameExcludeExtension(String filename) {
        // 파일 이름에 점이 있는지 확인하고, 마지막 점의 위치를 찾습니다.
        int dotIndex = filename.lastIndexOf('.');

        // 점이 없거나 파일 이름의 마지막에 위치하는 경우 빈 문자열을 반환합니다.
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(0, dotIndex);
    }


    public void deleteFile(Long cardId) {
        //같은 cardId 이전 업로드 확장자가 다를 경우 파일이 두개가 될 수 있음으로 제거 시도
        try {
            String search = cardId + ".*";
            DirectoryStream<Path> stream = Files.newDirectoryStream(fileLocation, search);
            for (Path entry : stream) {
                Files.delete(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
