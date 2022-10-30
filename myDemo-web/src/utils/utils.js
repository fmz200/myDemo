export const isNotNullORBlank = (...args)=> {
  for (var i = 0; i < args.length; i++) {
    var argument = args[i];
    if (argument == null || argument == '' || argument == undefined) {
      return false;
    }
  }
  return true;
}

export const isEmpty = (argument)=> {
  return argument == null || argument == '' || argument == undefined;
}

export const nalValue = (argument)=> {
  return isEmpty(argument) ? "" : argument;
}

