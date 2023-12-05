package io.github.xitssky.minio.configuration.properties;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The Minio Bucket configuration
 *
 * @author quentin
 */
@Getter
@Setter
public class MinioBucket {
    /**
     * The name of the bucket
     */
    private String name;

    /**
     * Whether the versioning is enabled or not
     */
    private boolean versioning = false;

    /**
     * Whether the object locking is enabled or not
     */
    private boolean objectLocking = false;

    /**
     * The retention policy to apply to the bucket
     */
    private MinioBucketRetention retention;

    /**
     * The access policies to apply to the bucket
     */
    private List<String> policies = new ArrayList<>();
}
