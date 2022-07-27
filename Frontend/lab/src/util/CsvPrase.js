import Papa from 'papaparse'

export function csvParse(file){
  return new Promise((resolve,reject)=>{
    Papa.parse(file,{
      header:true,
      delimiter: "",// auto-detect

      newline: "",// auto-detect
      skipEmptyLines:true,
      complete:(results)=>{resolve(results);},
      error:()=>{reject();}
    })
  })
}