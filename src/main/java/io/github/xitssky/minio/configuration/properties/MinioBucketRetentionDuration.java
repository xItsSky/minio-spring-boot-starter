package io.github.xitssky.minio.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * The Minio Bucket retention duration configuration
 *
 * @author quentin
 */
@Getter
@Setter
public class MinioBucketRetentionDuration {
    /**
     * The unit of the duration (DAYS | YEARS)
     */
    private String unit;

    /**
     * The duration of the retention
     */
    private int value;
}
