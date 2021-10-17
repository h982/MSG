package com.web.c101.file;

import com.web.c101.error.CustomException;
import com.web.c101.error.ErrorCode;
import com.web.c101.file.Response.FileResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {

    ImgFileDao imgFileDao;

    public Resource getFile(@PathVariable final String fileName) {
        Resource resource =  new FileSystemResource("//home//ubuntu//upload//"+fileName);
        return resource;
    }

    public FileResponse getFileNameList(long rid) {
        FileResponse fileResponse;

        try {
            List<ImgFile> imgFileList = imgFileDao.findFileByRid(rid);
            List<String> fileNameList = new ArrayList<>();

            for (ImgFile imgFile : imgFileList) {
                fileNameList.add(imgFile.getFile_name());
            }

            fileResponse = FileResponse.builder().fileNameList(fileNameList).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.ERROR);
        }

        return fileResponse;
    }

    @Transactional
    public void addFile(ImgFileDto newFile) throws IllegalStateException, IOException {

        String path = "//home//ubuntu//upload";
        File Folder = new File(path);
        if(!Folder.exists()) Folder.mkdir();

        String fileName;
        MultipartFile[] multipartFiles;

        if(newFile.getFiles() != null) {//파일이 존재할 때에만,
            multipartFiles = newFile.getFiles();

            for (int i = 0; i < multipartFiles.length; i++) {

                MultipartFile multipartFile = multipartFiles[i];
                UUID uuid = UUID.randomUUID();

                fileName = uuid.toString()+"_"+multipartFile.getOriginalFilename();
                multipartFile.transferTo(new File( path +"//"+fileName));
                ImgFile file = ImgFile.builder().
                        file_name(fileName).
                        file_size(Long.toString(multipartFile.getSize())).
                        rid(newFile.getRid()).build();

                imgFileDao.save(file);
            }
        }
    }

    @Transactional
    public void deleteFile(long rid) throws IllegalStateException, IOException{

        List<ImgFile> imgList = imgFileDao.findFileByRid(rid); //연관된 파일 검색

        for (ImgFile imgFile : imgList) {
            File file = new File("//home//ubuntu//upload//"+imgFile.getFile_name()); //연관된 파일 삭제
            if(file.exists()) file.delete();
        }
    }
}
