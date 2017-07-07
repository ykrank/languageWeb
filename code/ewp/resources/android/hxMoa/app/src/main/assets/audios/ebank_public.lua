ebank_public = {};

(function()

  local function init()

      local format_lib = {};
      function format_lib:public_header(str)
          local content = [[<label>12321312321</label>]];
          return content;
      end;
      ebank_public.format_lib = format_lib;
  end;
  init();
end)();
