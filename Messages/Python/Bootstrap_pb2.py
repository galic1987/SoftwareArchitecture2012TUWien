# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Bootstrap.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)


import general_pb2


DESCRIPTOR = _descriptor.FileDescriptor(
  name='Bootstrap.proto',
  package='',
  serialized_pb='\n\x0f\x42ootstrap.proto\x1a\rgeneral.proto\")\n\x10\x42ootstrapRequest\x12\x15\n\rnumberOfPeers\x18\x01 \x02(\x05\",\n\x11\x42ootstrapResponse\x12\x17\n\x04\x64\x61ta\x18\x01 \x03(\x0b\x32\t.PeerData')




_BOOTSTRAPREQUEST = _descriptor.Descriptor(
  name='BootstrapRequest',
  full_name='BootstrapRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='numberOfPeers', full_name='BootstrapRequest.numberOfPeers', index=0,
      number=1, type=5, cpp_type=1, label=2,
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
  serialized_start=34,
  serialized_end=75,
)


_BOOTSTRAPRESPONSE = _descriptor.Descriptor(
  name='BootstrapResponse',
  full_name='BootstrapResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='data', full_name='BootstrapResponse.data', index=0,
      number=1, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
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
  serialized_start=77,
  serialized_end=121,
)

_BOOTSTRAPRESPONSE.fields_by_name['data'].message_type = general_pb2._PEERDATA
DESCRIPTOR.message_types_by_name['BootstrapRequest'] = _BOOTSTRAPREQUEST
DESCRIPTOR.message_types_by_name['BootstrapResponse'] = _BOOTSTRAPRESPONSE

class BootstrapRequest(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _BOOTSTRAPREQUEST

  # @@protoc_insertion_point(class_scope:BootstrapRequest)

class BootstrapResponse(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _BOOTSTRAPRESPONSE

  # @@protoc_insertion_point(class_scope:BootstrapResponse)


# @@protoc_insertion_point(module_scope)
