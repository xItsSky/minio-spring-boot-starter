package io.github.xitssky.minio.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * The Minio Bucket retention configuration
 *
 * @author quentin
 */
@Getter
@Setter
public class MinioBucketRetention {
    /**
     * Whether the retention enabled or bot
     */
    private boolean enabled = false;

    /**
     * The retention mode (COMPLIANCE | GOVERNANCE)
     */
    private String mode;

    /**
     * The retention duration
     */
    private MinioBucketRetentionDuration duration;
}
