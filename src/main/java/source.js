db.temperature.aggregate([
    // Сортируем документы по _id в порядке убывания (последние 5 замеров)
    {
        $sort: { _id: -1 }
    },
    // Ограничиваем выборку последними 5 документами
    {
        $limit: 5
    },
    // Группируем документы и вычисляем min, max
    {
        $group: {
            _id: null, // Группируем все документы в одну группу
            max: { $max: "$value" }, // Максимальное значение
            min: { $min: "$value" }, // Минимальное значение
        }
    },
    // Преобразуем результат в нужный формат
    {
        $project: {
            _id: 0, // Исключаем поле _id
            max: 1,
            min: 1,
        }
    },
    // Сохраняем результат в коллекцию res
    {
        $merge: {
            into: "res", // Коллекция, в которую сохраняем результат
            whenMatched: "merge", // Если документ с таким _id уже существует, объединяем
            whenNotMatched: "insert" // Если документа с таким _id нет, вставляем новый
        }
    }
]);