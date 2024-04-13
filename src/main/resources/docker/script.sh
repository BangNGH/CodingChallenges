#!/bin/bash

compiler=$COMPILER
file=$FILE

# Kiểm tra xem tập tin input.txt có tồn tại không
if [ -f "/tmp/input.txt" ]; then
  # Đọc giá trị của input từ file input.txt
  input=$(</tmp/input.txt)
else
  # Nếu tập tin không tồn tại, thiết lập input là rỗng
  input=""
fi

echo "{" >/tmp/result.json

# Chạy file với giá trị của input
#output=$("$compiler" "$file" <<< "$input" 2>&1)
START=$(date +%s.%4N)
output=$(timeout 5s "$compiler" "$file" <<<"$input" 2>&1)
END=$(date +%s.%4N)
status=$?
echo " \"status\": \"$status \", " >>/tmp/result.json
# Kiểm tra kết quả của timeout
if [ $status -eq 124 ]; then
  # Nếu quá thời gian, ghi thông báo vào file JSON và kết thúc
  echo "\"Message\": \"Time exceeded\"," >>/tmp/result.json
  echo "}" >>/tmp/result.json
  exit
fi

# Kiểm tra xem có lỗi không
if [ $status -eq 0 ]; then
  if echo "$output" | grep -q "error(s)"; then
    echo " \"Message\": \"Compilation Failed\", " >>/tmp/result.json
  else
    echo " \"Message\": \"Success\", " >>/tmp/result.json
  fi

  # Nếu không có lỗi, mã hoá output và ghi vào file
  echo -n "\"stdout\": \"" >>/tmp/result.json
  echo -n "$output" | base64 | tr -d '\n' >>/tmp/result.json
  echo "\"," >>/tmp/result.json
else
  # Nếu có lỗi, ghi lỗi vào file
  echo " \"Message\": \" Compilation Failed\", " >>/tmp/result.json
  echo -n "\"error\": \"" >>/tmp/result.json
  echo -n "$output" | base64 | tr -d '\n' >>/tmp/result.json
  echo "\"," >>/tmp/result.json
fi

runtime=$(echo "$END - $START" | bc)

echo "\"time\": \"$runtime\"" >>/tmp/result.json
echo "}" >>/tmp/result.json
