SRC_DIR :=

ifeq ($(CONFIG_USING_CMSIS_5),y)
	SRC_DIR += cmsis_5
endif
ifeq ($(CONFIG_USING_KNOWING_FILTER),y)
	SRC_DIR += filter
endif
ifeq ($(CONFIG_USING_IMAGE_PROCESSING),y)
	SRC_DIR += image_processing
endif
ifeq ($(CONFIG_USING_KPU_PROCESSING),y)
	SRC_DIR += kpu
endif
ifeq ($(CONFIG_USING_NNOM),y)
	SRC_DIR += nnom
endif
ifeq ($(CONFIG_USING_OTA_MODEL),y)
	SRC_DIR += ota
endif
ifeq ($(CONFIG_USING_TENSORFLOWLITEMICRO),y)
	SRC_DIR += tensorflow-lite
endif


include $(KERNEL_ROOT)/compiler.mk
