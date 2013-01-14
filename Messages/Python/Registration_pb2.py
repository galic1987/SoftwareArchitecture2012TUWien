# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Registration.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)




DESCRIPTOR = _descriptor.FileDescriptor(
  name='Registration.proto',
  package='',
  serialized_pb='\n\x12Registration.proto\"<\n\x13RegisterPeerRequest\x12\x13\n\x0bPeerAddress\x18\x01 \x02(\t\x12\x10\n\x08\x43lientID\x18\x02 \x02(\x05\"\x89\x01\n\x14RegisterPeerResponse\x12\x0e\n\x06PeerID\x18\x01 \x01(\x05\x12\x38\n\x06status\x18\x02 \x01(\x0e\x32(.RegisterPeerResponse.RegistrationStatus\"\'\n\x12RegistrationStatus\x12\x06\n\x02OK\x10\x00\x12\t\n\x05\x45rror\x10\x01')



_REGISTERPEERRESPONSE_REGISTRATIONSTATUS = _descriptor.EnumDescriptor(
  name='RegistrationStatus',
  full_name='RegisterPeerResponse.RegistrationStatus',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='OK', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='Error', index=1, number=1,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=183,
  serialized_end=222,
)


_REGISTERPEERREQUEST = _descriptor.Descriptor(
  name='RegisterPeerRequest',
  full_name='RegisterPeerRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='PeerAddress', full_name='RegisterPeerRequest.PeerAddress', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='ClientID', full_name='RegisterPeerRequest.ClientID', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=22,
  serialized_end=82,
)


_REGISTERPEERRESPONSE = _descriptor.Descriptor(
  name='RegisterPeerResponse',
  full_name='RegisterPeerResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='PeerID', full_name='RegisterPeerResponse.PeerID', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='status', full_name='RegisterPeerResponse.status', index=1,
      number=2, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _REGISTERPEERRESPONSE_REGISTRATIONSTATUS,
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=85,
  serialized_end=222,
)

_REGISTERPEERRESPONSE.fields_by_name['status'].enum_type = _REGISTERPEERRESPONSE_REGISTRATIONSTATUS
_REGISTERPEERRESPONSE_REGISTRATIONSTATUS.containing_type = _REGISTERPEERRESPONSE;
DESCRIPTOR.message_types_by_name['RegisterPeerRequest'] = _REGISTERPEERREQUEST
DESCRIPTOR.message_types_by_name['RegisterPeerResponse'] = _REGISTERPEERRESPONSE

class RegisterPeerRequest(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _REGISTERPEERREQUEST

  # @@protoc_insertion_point(class_scope:RegisterPeerRequest)

class RegisterPeerResponse(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _REGISTERPEERRESPONSE

  # @@protoc_insertion_point(class_scope:RegisterPeerResponse)


# @@protoc_insertion_point(module_scope)
