package org.platformv1.platformccv1.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceListingResponse {
    private Long id;
    private Long ownerId;
    private String ownerName;
    private String title;
    private String description;
    private Double price;
    private String tags;
    private Integer deliveryTime;

    private String status;
    private long viewsCount;
}
