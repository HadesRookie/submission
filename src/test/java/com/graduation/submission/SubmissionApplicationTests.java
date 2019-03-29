package com.graduation.submission;

import com.graduation.submission.pojo.Permission;
import com.graduation.submission.service.PermissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubmissionApplicationTests {

    @Autowired
    private PermissionService permissionService;

    @Test
    public void contextLoads() {
        List<Permission> permissions = new ArrayList<>();

        permissions = permissionService.findUserPermissions(1);

        System.out.println(permissions.toString());

    }

}
