import os
from PIL import Image

# 设置输入和输出目录
input_dir = "D:\\006-Programing\\CS109_ChinaChess\\pictures\\input"   # 存放原始512x512 PNG图像的文件夹
output_dir = "D:\\006-Programing\\CS109_ChinaChess\\pictures\\output" # 保存缩放后90x90 PNG图像的文件夹

# 创建输出目录（如果不存在）
os.makedirs(output_dir, exist_ok=True)

# 遍历输入目录中的所有文件
for filename in os.listdir(input_dir):
    if filename.lower().endswith(".png"):
        input_path = os.path.join(input_dir, filename)
        output_path = os.path.join(output_dir, filename)

        try:
            with Image.open(input_path) as img:
                # 可选：检查原始图像是否为512x512（非必需）
                # if img.size != (512, 512):
                #     print(f"跳过非512x512图像: {filename}")
                #     continue

                # 缩放到90x90，使用高质量的抗锯齿算法
                resized_img = img.resize((90, 90), Image.LANCZOS)
                resized_img.save(output_path, "PNG")
                print(f"已处理: {filename}")
        except Exception as e:
            print(f"处理 {filename} 时出错: {e}")

print("批量转换完成！")