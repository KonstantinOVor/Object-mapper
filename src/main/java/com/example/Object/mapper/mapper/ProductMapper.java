package com.example.Object.mapper.mapper;
import com.example.Object.mapper.model.Product;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product mapToProduct(Product product);
}
