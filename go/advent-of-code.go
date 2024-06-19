package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	fmt.Println("Advent of code day 5")

	// Open input
	input, err := openFile("inputs/5/input.txt")
	if err != nil {
		return
	}

	// Parse input into Maps
	var maps []Map
	lines := strings.Split(strings.TrimSuffix(input, "\r\n"), "\r\n")
	var seeds []int

	for index, line := range lines {
		if index == 0 {
			// this line has the seeds
			seedsAsStrings := line[7:]
			seeds = asIntSlice(seedsAsStrings)
		} else {
			if strings.Contains(line, "map:") {
				temp := index + 1
				var rows []Row
				for temp < len(lines) && len(lines[temp]) > 0 {
					rawnums := asIntSlice(lines[temp])
					row := Row{dst: rawnums[0], src: rawnums[1], len: rawnums[2]}
					rows = append(rows, row)
					temp = temp + 1
				}
				newMap := Map{
					name: line,
					rows: rows,
				}
				maps = append(maps, newMap)
			}
		}
	}

	var location int
	for _, seed := range seeds {
		currentMapping := seed
		for _, mp := range maps {
			currentMapping = getMapping(currentMapping, mp)
		}
		if location == 0 {
			location = currentMapping
		} else if currentMapping < location {
			location = currentMapping
		}
	}
	fmt.Println("Answer for Part 1: ", location)

	location = 0
	for i := 0; i < len(seeds); i += 2 {
		for seed := seeds[i]; seed <= seeds[i]+seeds[i+1]; seed += 1 {
			currentMapping := seed
			for _, mp := range maps {
				currentMapping = getMapping(currentMapping, mp)
			}
			if location == 0 {
				location = currentMapping
			} else if currentMapping < location {
				location = currentMapping
			}
		}
	}
	fmt.Println("Answer for Part 2: ", location)
}

func getMapping(value int, mp Map) int {
	for _, row := range mp.rows {
		if isInRow(value, row) {
			// perform mapping
			return value + row.dst - row.src
		}
	}
	return value
}

func isInRow(seed int, row Row) bool {
	end := row.src + row.len
	if seed >= row.src && seed < end {
		return true
	}

	return false
}

func asIntSlice(line string) []int {
	var output []int
	for _, num := range strings.Split(strings.Trim(line, " "), " ") {
		output = append(output, Handle(strconv.Atoi(num)))
	}

	return output
}

func openFile(filepath string) (string, error) {
	fileData, err := os.ReadFile(filepath)
	if err != nil {
		fmt.Println("Unable to open file:", err)
		return "", err
	}

	return string(fileData), nil
}

func Handle[T any](value T, err error) T {
	if err != nil {
		panic(err)
	} else {
		return value
	}
}

type Map struct {
	name string
	rows []Row
}

type Row struct {
	src int
	dst int
	len int
}
