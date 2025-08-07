package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.user.AddressDto;
import com.jobfinder.finder.entity.AddressEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  AddressEntity mapToAddressEntity(AddressDto addressDto);

  AddressDto mapToAddressDto(AddressEntity addressEntity);

  List<AddressEntity> mapToAddressEntityList(List<AddressDto> addressDto);

  List<AddressDto> mapToAddressDtoList(List<AddressEntity> addressEntity);
}
