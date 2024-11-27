package com.nineleaps.authentication.jwt.entityTesting;

import com.nineleaps.authentication.jwt.entity.Picture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PictureTest {

    @Test
    void testGettersAndSetters() {
        Picture picture = new Picture();
        String profileImage = "example.jpg";

        picture.setProfileImage(profileImage);

        assertEquals(profileImage, picture.getProfileImage());
    }
}
